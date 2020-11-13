package com.hfhk.cairo.auth.service.security.oauth2.server.resource.authentication;

import cn.hutool.core.util.IdUtil;
import com.hfhk.cairo.security.authentication.*;
import com.hfhk.cairo.core.auth.RoleConstant;
import com.hfhk.cairo.security.oauth2.server.resource.authentication.CairoAuthenticationToken;
import com.hfhk.cairo.security.oauth2.spec.ClientSpec;
import com.hfhk.cairo.security.oauth2.spec.UserSpec;
import com.hfhk.cairo.auth.service.module.auth.constants.Redis;
import com.hfhk.cairo.auth.service.module.auth.domain.mongo.DepartmentMongo;
import com.hfhk.cairo.auth.service.module.auth.domain.mongo.ResourceMongo;
import com.hfhk.cairo.auth.service.module.auth.domain.mongo.RoleMongo;
import com.hfhk.cairo.auth.service.module.auth.domain.mongo.UserMongo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Duration;
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
		Client client = convertClient(source);
		String uid = convertUid(source);
		if (Optional.ofNullable(uid).filter(x -> !x.isBlank()).isPresent()) {
			final String redisKey = Redis.Token.key(client.getId(), uid);
			final RemoteUser data = Optional.ofNullable(redisTemplate.opsForValue().get(redisKey))
				.orElseGet(() ->
					dataFindUser(client.getId(), uid)
						.map(user -> {
							final Collection<GrantedAuthority> authorities = dataFindUserAuthority(client.getId(), uid);
							final RemoteUser newAuthentication = new RemoteUser(user, Optional.ofNullable(authorities)
								.stream()
								.flatMap(Collection::stream)
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()));

							redisTemplate.opsForValue().set(redisKey, newAuthentication, Duration.ofHours(1L));
							return newAuthentication;
						}).orElse(new RemoteUser())
				);
			return new CairoAuthenticationToken(source, client, data.getUser(), Optional.ofNullable(data.getAuthorities())
				.stream()
				.flatMap(Collection::stream)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList()));
		} else {
			return new CairoAuthenticationToken(source, client, null, Collections.emptyList());
		}
	}

	private Client convertClient(Jwt jwt) {
		Map<String, Object> claims = Optional.ofNullable(jwt.getClaims()).orElse(Collections.emptyMap());
		return Client.builder()
			.id(claims.getOrDefault(ClientSpec.ID, "default").toString())
			.scopes((Collection<String>) claims.getOrDefault(ClientSpec.SCOPE, Collections.emptyList()))
			.build();
	}

	private String convertUid(Jwt jwt) {
		return Optional.ofNullable(jwt.getClaims())
			.map(claims -> claims.get(UserSpec.UID))
			.map(x -> (String) x)
			.orElse(IdUtil.objectId());
	}

	/**
	 * 查询用户
	 *
	 * @param client client
	 * @param uid    uid
	 * @return i
	 */
	private Optional<User> dataFindUser(String client, String uid) {
		return Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where("uid").is(uid)), UserMongo.class))
			.map(x -> {
				List<Role> roles = Optional.ofNullable(x.getClientRoles()).filter(y -> y.containsKey(client))
					.map(y -> y.get(client))
					.map(codes ->
						mongoTemplate.find(Query.query(Criteria.where("code").in(codes)), RoleMongo.class)
							.stream()
							.map(role -> Role.builder().code(role.getCode()).name(role.getName()).build())
							.collect(Collectors.toList())
					)
					.orElse(Collections.emptyList());
				List<Department> departments = Optional.ofNullable(x.getClientDepartments()).filter(y -> y.containsKey(client))
					.map(y -> y.get(client))
					.map(ids ->
						mongoTemplate.find(Query.query(Criteria.where("_id").in(ids)), DepartmentMongo.class)
							.stream()
							.map(role -> Department.builder().id(role.getId()).name(role.getName()).build())
							.collect(Collectors.toList())
					)
					.orElse(Collections.emptyList());
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
	 * @param client client
	 * @param user   user
	 * @return 权限 列表
	 */
	private Collection<GrantedAuthority> dataFindUserAuthority(String client, String user) {
		return Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where("uid").is(user)), UserMongo.class))
			.stream()
			.flatMap(x -> {

				Set<String> roleCodes = Optional.ofNullable(x.getClientRoles())
					.filter(y -> y.containsKey(client))
					.flatMap(y -> Optional.ofNullable(y.get(client)))
					.stream().flatMap(Collection::stream)
					.collect(Collectors.toSet());

				boolean isAdmin = roleCodes.contains(RoleConstant.ADMIN);
				List<RoleMongo> roles = mongoTemplate.find(Query.query(Criteria.where("code").in(roleCodes)), RoleMongo.class);
				Stream<String> userAuthorityStream = isAdmin
					?
					mongoTemplate.find(Query.query(Criteria.where("client").is(client)), ResourceMongo.class)
						.stream().flatMap(y -> Optional.ofNullable(y.getPermissions()).stream().flatMap(Collection::stream))
					:
					roles.stream()
						.flatMap(y -> Optional.ofNullable(y.getResources()).stream().flatMap(Collection::stream));
				Stream<String> roleAuthorityStream = roleCodes.stream().map("ROLE_"::concat);
				Stream<String> roleResourceAuthorityStream = roles.stream().flatMap(z -> Optional.ofNullable(z.getResources()).orElse(Collections.emptyList()).stream());

				return Stream.concat(
					userAuthorityStream,
					Stream.concat(roleAuthorityStream, roleResourceAuthorityStream));
			})
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toSet());
	}
}
