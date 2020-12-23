package com.hfhk.auth.service.authentication;

import com.hfhk.auth.domain.mongo.Mongo;
import com.hfhk.auth.domain.mongo.ResourceMongo;
import com.hfhk.auth.domain.mongo.RoleMongo;
import com.hfhk.auth.domain.mongo.UserMongo;
import com.hfhk.auth.service.constants.Redis;
import com.hfhk.cairo.core.auth.RoleConstant;
import com.hfhk.cairo.security.authentication.Department;
import com.hfhk.cairo.security.authentication.RemoteUser;
import com.hfhk.cairo.security.authentication.Role;
import com.hfhk.cairo.security.authentication.User;
import com.hfhk.cairo.security.oauth2.server.resource.authentication.CairoAuthenticationToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CairoJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
	private final MongoTemplate mongoTemplate;
	private final RedisTemplate<String, RemoteUser> redisTemplate;

	public CairoJwtAuthenticationConverter(MongoTemplate mongoTemplate, RedisTemplate<String, RemoteUser> redisTemplate) {
		this.mongoTemplate = mongoTemplate;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public AbstractAuthenticationToken convert(Jwt source) {
		String client = Optional.ofNullable(source.getClaimAsString(IdTokenClaimNames.AZP)).orElse("default");
		String uid = Optional.ofNullable(source.getSubject()).orElse("default");
		String key = Redis.Token.key(client, uid);
		RemoteUser remoteUser = Optional.ofNullable(redisTemplate.opsForValue().get(key))
			.orElseGet(() -> {
				Collection<String> authorities = dataFindUserAuthority(uid, Collections.singleton(client));
				User user = dataFindUser(uid, Collections.singleton(client)).orElse(null);
				RemoteUser newRemoteUser = new RemoteUser(user, authorities);

				Duration expire = Optional.ofNullable(source.getExpiresAt())
					.filter(x -> Instant.now().isAfter(x))
					.map(x -> Duration.of(Instant.now().until(x, ChronoUnit.MILLIS), ChronoUnit.MILLIS))
					.orElse(Duration.ofHours(2));

				redisTemplate.opsForValue().set(key, newRemoteUser, expire);
				return newRemoteUser;
			});
		return new CairoAuthenticationToken(
			source,
			remoteUser.getAuthorities().stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet()),
			remoteUser.getUser()
		);
	}

	/**
	 * @param uid     uid
	 * @param clients clients
	 * @return user
	 */
	private Optional<User> dataFindUser(String uid, Collection<String> clients) {
		return Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where("uid").is(uid)), UserMongo.class))
			.map(x -> {
				Map<String, Set<String>> clientRolesMap = Optional.ofNullable(x.getClientRoles()).orElse(Collections.emptyMap());
				Map<String, Set<String>> clientDepartmentMap = Optional.ofNullable(x.getClientDepartments()).orElse(Collections.emptyMap());
				Map<String, Collection<Role>> roleMap = clients.stream()
					.collect(Collectors.toMap(y -> y, y -> clientRolesMap.getOrDefault(y, Collections.emptySet())
						.stream()
						.map(z -> Role.builder()
							.code(z)
							.name(z)
							.build())
						.collect(Collectors.toList())));

				Map<String, Collection<Department>> departmentMap = clients.stream()
					.collect(Collectors.toMap(y -> y, y -> clientDepartmentMap.getOrDefault(y, Collections.emptySet())
						.stream()
						.map(z -> Department.builder()
							.id(z)
							.name(z)
							.build())
						.collect(Collectors.toList())));

				return User.builder()
					.uid(x.getUid())
					.username(x.getUsername())
					.phoneNumber(x.getPhoneNumber())
					.email(x.getEmail())
					.name(x.getName())
					.avatarUrl(x.getAvatarUrl())
					.roles(roleMap)
					.departments(departmentMap)
					.build();
			});
	}

	/**
	 * 查询权限
	 *
	 * @param uid     uid
	 * @param clients clients
	 * @return 权限
	 */
	private Collection<String> dataFindUserAuthority(String uid, Collection<String> clients) {
		return Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where("Uid").is(uid)), UserMongo.class, Mongo.Collection.User))
			.stream()
			.flatMap(user -> {
				Map<String, Set<String>> clientRolesMap = Optional.ofNullable(user.getClientRoles())
					.orElse(Collections.emptyMap());

				Map<String, Set<String>> clientResourceMap = Optional.ofNullable(user.getClientResources())
					.orElse(Collections.emptyMap());

				List<String> roleCodes = clients.stream()
					.flatMap(x -> clientRolesMap.getOrDefault(x, Collections.emptySet()).parallelStream())
					.collect(Collectors.toList());
				List<String> userResources = clients.stream()
					.flatMap(x -> clientResourceMap.getOrDefault(x, Collections.emptySet()).parallelStream())
					.collect(Collectors.toList());

				boolean isAdmin = roleCodes.contains(RoleConstant.ADMIN);
				List<RoleMongo> roles = mongoTemplate.find(Query.query(Criteria.where(RoleMongo.Field.Code).in(roleCodes)), RoleMongo.class);
				Stream<String> roleStream = roles.stream().map(RoleMongo::getCode)
					.map("ROLE_"::concat);
				Criteria resourceCriteria = new Criteria();
				if (isAdmin) {
					resourceCriteria.and(ResourceMongo.Field.Client).in(clients);
				} else {
					Set<String> resourceIds = roles.stream()
						.flatMap(x -> Optional.ofNullable(x.getResources()).stream())
						.flatMap(Collection::parallelStream)
						.collect(Collectors.toSet());
					if (!resourceIds.isEmpty()) {
						resourceCriteria.and(ResourceMongo.Field._ID).in(resourceIds);
					}
				}
				if (!userResources.isEmpty()) {
					resourceCriteria.orOperator(Criteria.where(ResourceMongo.Field._ID).in(userResources));
				}

				Query resourceQuery = Query.query(resourceCriteria);

				Stream<String> resourceStream = mongoTemplate.find(resourceQuery, ResourceMongo.class, Mongo.Collection.Resource)
					.stream()
					.flatMap(x -> Optional.ofNullable(x.getPermissions()).stream())
					.flatMap(Collection::parallelStream);

				return Stream.concat(roleStream, resourceStream);
			})
			.collect(Collectors.toSet());
	}
}
