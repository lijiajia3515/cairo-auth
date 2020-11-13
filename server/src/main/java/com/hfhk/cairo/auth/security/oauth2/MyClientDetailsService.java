package com.hfhk.cairo.auth.security.oauth2;

import com.hfhk.cairo.auth.security.domain.mongo.ClientMongo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import java.util.Optional;

public class MyClientDetailsService extends AbstractClientDetailsService implements ClientDetailsService {
	public MyClientDetailsService(MongoTemplate mongoTemplate) {
		super(mongoTemplate);
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		ClientMongo clientMongo = mongoTemplate.findOne(
			Query.query(Criteria.where("clientId").is(clientId)),
			ClientMongo.class
		);

		return Optional.ofNullable(clientMongo)
			.map(this::buildClient)
			.orElseThrow(() -> new NoSuchClientException("客户端未找到"))
			;
	}
}
