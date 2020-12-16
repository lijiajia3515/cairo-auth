package com.hfhk.auth.server.userdetails;

import com.hfhk.auth.server.domain.mongo.UserMongo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;

public class AuthPasswordService extends AbstractUserDetailsService implements UserDetailsPasswordService {

	public AuthPasswordService(MongoTemplate mongoTemplate) {
		super(mongoTemplate);
	}

	@Override
	public UserDetails updatePassword(UserDetails user, String newPassword) {
		mongoTemplate.findAndModify(
			Query.query(Criteria.where("uid").is(user.getUsername())),
			BasicUpdate.update("password", newPassword),
			UserMongo.class
		);
		return user;
	}
}
