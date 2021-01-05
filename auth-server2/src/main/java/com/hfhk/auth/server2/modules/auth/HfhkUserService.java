package com.hfhk.auth.server2.modules.auth;

import com.hfhk.auth.domain.mongo.Mongo;
import com.hfhk.auth.domain.mongo.ResourceMongo;
import com.hfhk.auth.domain.mongo.UserMongo;
import com.hfhk.auth.server2.Constant;
import com.hfhk.auth.server2.domain.AuthUser;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HfhkUserService implements UserDetailsService {
	public static final String ROLE_PREFIX = "ROLE_";
	private final MongoTemplate mongoTemplate;

	public HfhkUserService(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Query query = Query.query(
			new Criteria()
				.orOperator(
					Criteria.where(UserMongo.FIELD.UID).is(username),
					Criteria.where(UserMongo.FIELD.USERNAME).is(username),
					Criteria.where(UserMongo.FIELD.PHONE_NUMBER).is(username),
					Criteria.where(UserMongo.FIELD.EMAIL).is(username)
				)
		);
		UserMongo userMongo = mongoTemplate.findOne(query, UserMongo.class, Mongo.Collection.USER);
		return Optional.ofNullable(userMongo)
			.map(user -> {
				Set<String> roleCodes = Optional.ofNullable(user.getClientRoles()).orElse(Collections.emptyMap()).getOrDefault(Constant.CLIENT, Collections.emptySet());
				Set<String> resourceCodes = Optional.ofNullable(user.getClientResources()).orElse(Collections.emptyMap()).getOrDefault(Constant.CLIENT, Collections.emptySet());
				Set<String> departmentCodes = Optional.ofNullable(user.getClientDepartments()).orElse(Collections.emptyMap()).getOrDefault(Constant.CLIENT, Collections.emptySet());

				Criteria resourceCriteria = Criteria.where(ResourceMongo.FIELD.METADATA.DELETED).is(0L);
				if (roleCodes.contains(Constant.ROLE_ADMIN)) {
					resourceCriteria.and(ResourceMongo.FIELD.CLIENT).is(Constant.CLIENT);
				} else {
					resourceCriteria.and(ResourceMongo.FIELD._ID).is(resourceCodes);
				}
				if (!resourceCodes.isEmpty()) {
					resourceCriteria.orOperator(
						Criteria.where(ResourceMongo.FIELD.METADATA.DELETED).is(0L)
							.and(ResourceMongo.FIELD._ID).in(resourceCodes)
					);
				}

				List<ResourceMongo> resources = mongoTemplate.find(Query.query(resourceCriteria), ResourceMongo.class, Mongo.Collection.RESOURCE);

				Stream<String> resourceStream = resources.stream()
					.flatMap(x -> Optional.ofNullable(x.getPermissions()).orElse(Collections.emptySet()).parallelStream());

				Collection<GrantedAuthority> authorities = Stream.concat(roleCodes.stream().map(ROLE_PREFIX::concat), resourceStream)
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toSet());

				return AuthUser.builder()
					.uid(user.getUid())
					.username(user.getUsername())
					.password(user.getPassword())
					.phoneNumber(user.getPhoneNumber())
					.email(user.getEmail())
					.roles(roleCodes)
					.departments(departmentCodes)
					.authorities(authorities)
					.accountEnabled(user.getAccountEnabled())
					.accountLocked(user.getAccountLocked())
					.build();
			})
			.orElseThrow(() -> new UsernameNotFoundException("用户名或密码错误"));

	}
}
