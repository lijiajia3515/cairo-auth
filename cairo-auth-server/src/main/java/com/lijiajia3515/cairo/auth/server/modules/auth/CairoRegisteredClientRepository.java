package com.lijiajia3515.cairo.auth.server.modules.auth;

import com.lijiajia3515.auth.domain.mongo.ClientMongo;
import com.lijiajia3515.auth.domain.mongo.Mongo;
import com.lijiajia3515.cairo.auth.server.modules.client.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.Optional;

public class CairoRegisteredClientRepository implements RegisteredClientRepository {
	private final MongoTemplate mongoTemplate;

	public CairoRegisteredClientRepository(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public RegisteredClient findById(String id) {
		Query query = Query.query(Criteria
			.where(ClientMongo.FIELD.ENABLED).is(true)
			.and(ClientMongo.FIELD._ID).is(id)
		);

		ClientMongo client = mongoTemplate.findOne(query, ClientMongo.class, Mongo.Collection.CLIENT);

		return Optional.ofNullable(client).map(Converter::convert).orElse(null);
	}

	@Override
	public RegisteredClient findByClientId(String clientId) {
		Query query = Query.query(Criteria
			.where(ClientMongo.FIELD.ENABLED).is(true)
			.and(ClientMongo.FIELD.CLIENT_ID).is(clientId)
		);

		ClientMongo client = mongoTemplate.findOne(query, ClientMongo.class, Mongo.Collection.CLIENT);

		return Optional.ofNullable(client).map(Converter::convert).orElse(null);
	}

}
