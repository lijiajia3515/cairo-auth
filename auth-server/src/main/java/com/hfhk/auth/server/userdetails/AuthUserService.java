package com.hfhk.auth.server.userdetails;

import com.hfhk.auth.domain.mongo.Mongo;
import com.hfhk.auth.domain.mongo.UserMongo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class AuthUserService extends AbstractUserDetailsService implements UserDetailsService {

	public AuthUserService(MongoTemplate mongoTemplate) {
		super(mongoTemplate);
	}

	@Override
	public AuthUser loadUserByUsername(String username) throws UsernameNotFoundException {
		Criteria cUid = Criteria.where(UserMongo.FIELD.UID).is(username);
		Criteria cUsername = Criteria.where(UserMongo.FIELD.USERNAME).is(username);
		Criteria cPhoneNumber = Criteria.where(UserMongo.FIELD.PHONE_NUMBER).is(username);
		Criteria cEmail = Criteria.where(UserMongo.FIELD.EMAIL).is(username);

		UserMongo userMongo = mongoTemplate.findOne(
			Query.query(new Criteria().orOperator(cUid, cUsername, cPhoneNumber, cEmail)),
			UserMongo.class,
			Mongo.Collection.USER
		);
		return Optional.ofNullable(userMongo)
			.map(this::buildAuthUser)
			.orElseThrow(() -> new UsernameNotFoundException("用户名密码不存在"));
	}
}
