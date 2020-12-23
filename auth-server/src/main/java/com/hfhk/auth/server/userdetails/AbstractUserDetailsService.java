package com.hfhk.auth.server.userdetails;

import com.hfhk.auth.domain.mongo.UserMongo;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Optional;

public abstract class AbstractUserDetailsService {
	protected final MongoTemplate mongoTemplate;

	protected AbstractUserDetailsService(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public AuthUser buildAuthUser(UserMongo user) {
		return AuthUser.builder()
			.uid(user.getUid())
			.username(user.getUsername())
			.phoneNumber(user.getPhoneNumber())
			.email(user.getEmail())
			.password(user.getPassword())
			//.authorities(Optional.of(user.getAuthorities()).orElse(Collections.emptyList()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
			.accountEnabled(Optional.ofNullable(user.getAccountEnabled()).orElse(false))
			.accountLocked(Optional.ofNullable(user.getAccountLocked()).orElse(false))
			.build();
	}
}
