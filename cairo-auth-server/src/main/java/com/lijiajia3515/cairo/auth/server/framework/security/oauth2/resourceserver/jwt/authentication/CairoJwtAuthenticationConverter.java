package com.lijiajia3515.cairo.auth.server.framework.security.oauth2.resourceserver.jwt.authentication;

import com.lijiajia3515.cairo.auth.domain.mongo.Mongo;
import com.lijiajia3515.cairo.auth.domain.mongo.ResourceMongo;
import com.lijiajia3515.cairo.auth.domain.mongo.RoleMongo;
import com.lijiajia3515.cairo.auth.domain.mongo.UserMongo;
import com.lijiajia3515.cairo.auth.server.constants.Redis;
import com.lijiajia3515.cairo.domain.auth.Department;
import com.lijiajia3515.cairo.domain.auth.Role;
import com.lijiajia3515.cairo.domain.auth.User;
import com.lijiajia3515.cairo.security.SecurityConstants;
import com.lijiajia3515.cairo.security.authentication.RemoteUser;
import com.lijiajia3515.cairo.security.oauth2.server.resource.authentication.CairoAuthentication;
import com.lijiajia3515.cairo.security.oauth2.user.AuthPrincipal;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CairoJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
	private final MongoTemplate mongoTemplate;
	private final RedisTemplate<String, Object> redisTemplate;

	public CairoJwtAuthenticationConverter(MongoTemplate mongoTemplate, RedisTemplate<String, Object> redisTemplate) {
		this.mongoTemplate = mongoTemplate;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public AbstractAuthenticationToken convert(Jwt source) {
		String client = source.getAudience().stream().findFirst().orElse("default");
		String uid = Optional.ofNullable(source.getSubject()).orElse("default");
		String key = Redis.UserInfo.key(client, uid);
		RemoteUser remoteUser = Optional.ofNullable((RemoteUser) redisTemplate.opsForValue().get(key))
			.orElseGet(() -> {
				Collection<String> authorities = findDbUserAuthority(uid, client);
				User user = findDbUser(uid, client).orElse(null);
				RemoteUser newRemoteUser = new RemoteUser(user, authorities);

				Duration expire = Optional.ofNullable(source.getExpiresAt())
					.filter(x -> Instant.now().isAfter(x))
					.map(x -> Duration.of(Instant.now().until(x, ChronoUnit.MILLIS), ChronoUnit.MILLIS))
					.orElse(Duration.ofHours(2));

				redisTemplate.opsForValue().set(key, newRemoteUser, expire);
				return newRemoteUser;
			});
		return new CairoAuthentication(
			new AuthPrincipal(source, remoteUser.getUser()),
			remoteUser.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
		);
	}

	/**
	 * @param uid    uid
	 * @param client client
	 * @return user
	 */
	private Optional<User> findDbUser(String uid, String client) {
		return Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where(UserMongo.FIELD.UID).is(uid)), UserMongo.class, Mongo.Collection.USER))
			.map(x -> {
				Set<String> roleCodes = Optional.ofNullable(x.getClientRoles())
					.map(y -> y.getOrDefault(client, Collections.emptySet()))
					.orElse(Collections.emptySet());
				Set<String> departmentCodes = Optional.ofNullable(x.getClientDepartments())
					.map(y -> y.getOrDefault(client, Collections.emptySet()))
					.orElse(Collections.emptySet());

				List<Role> roles = roleCodes.stream()
					.map(y -> Role.builder().id(y).name(y).build())
					.collect(Collectors.toList());
				List<Department> departments = departmentCodes.stream()
					.map(y -> Department.builder().id(y).name(y).build())
					.collect(Collectors.toList());

				return User.builder()
					.uid(x.getUid())
					.username(x.getUsername())
					.phoneNumber(x.getPhoneNumber())
					.email(x.getEmail())
					.name(x.getName())
					.avatarUrl(x.getAvatarUrl())
					.roles(roles)
					.departments(departments)
					.build();
			});
	}

	/**
	 * 查询权限
	 *
	 * @param uid    uid
	 * @param client client
	 * @return 权限
	 */
	private Collection<String> findDbUserAuthority(String uid, String client) {
		return Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where(UserMongo.FIELD.UID).is(uid)), UserMongo.class, Mongo.Collection.USER))
			.stream()
			.flatMap(user -> {
				Set<String> roleCodes = Optional.ofNullable(user.getClientRoles())
					.orElse(Collections.emptyMap())
					.getOrDefault(client, Collections.emptySet());


				Set<String> userResourceIds = Optional.ofNullable(user.getClientResources())
					.orElse(Collections.emptyMap())
					.getOrDefault(client, Collections.emptySet());


				boolean isAdmin = roleCodes.contains(SecurityConstants.Role.ADMIN);
				List<RoleMongo> roles = mongoTemplate.find(Query.query(Criteria.where(RoleMongo.FIELD.CODE).in(roleCodes)), RoleMongo.class);
				Stream<String> roleStream = Stream.concat(Stream.of("USER"), roles.parallelStream().map(RoleMongo::getCode))
					.map("ROLE_"::concat);
				Criteria resourceCriteria = new Criteria();
				if (isAdmin) {
					resourceCriteria.and(ResourceMongo.FIELD.CLIENT).is(client);
				} else {
					Set<String> resourceIds = roles.stream()
						.flatMap(x -> Optional.ofNullable(x.getResources()).stream())
						.flatMap(Collection::parallelStream)
						.collect(Collectors.toSet());
					if (!resourceIds.isEmpty()) {
						resourceCriteria.and(ResourceMongo.FIELD._ID).in(resourceIds);
					}
				}
				if (!userResourceIds.isEmpty()) {
					resourceCriteria.orOperator(Criteria.where(ResourceMongo.FIELD._ID).in(userResourceIds));
				}

				Query resourceQuery = Query.query(resourceCriteria);

				Stream<String> resourceStream = mongoTemplate.find(resourceQuery, ResourceMongo.class, Mongo.Collection.RESOURCE)
					.stream()
					.flatMap(x -> Optional.ofNullable(x.getPermissions()).stream())
					.flatMap(Collection::parallelStream);

				return Stream.concat(roleStream, resourceStream);
			})
			.collect(Collectors.toSet());
	}
}
