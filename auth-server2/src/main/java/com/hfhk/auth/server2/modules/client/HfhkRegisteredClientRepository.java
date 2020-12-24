package com.hfhk.auth.server2.modules.client;

import com.hfhk.auth.domain.mongo.ClientMongo;
import com.hfhk.auth.domain.mongo.Mongo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.Optional;

public class HfhkRegisteredClientRepository implements RegisteredClientRepository {
	private final MongoTemplate mongoTemplate;

	public HfhkRegisteredClientRepository(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public RegisteredClient findById(String id) {
		Query query = Query.query(Criteria.where(ClientMongo.Field.Enabled).is(true).and(ClientMongo.Field._ID).is(id));

		ClientMongo client = mongoTemplate.findOne(query, ClientMongo.class, Mongo.Collection.Client);

		return Optional.ofNullable(client).map(Converter::convert).orElse(null);
	}

	@Override
	public RegisteredClient findByClientId(String clientId) {
		Query query = Query.query(Criteria.where(ClientMongo.Field.Enabled).is(true).and(ClientMongo.Field.ClientId).is(clientId));

		ClientMongo client = mongoTemplate.findOne(query, ClientMongo.class, Mongo.Collection.Client);

		return Optional.ofNullable(client).map(Converter::convert).orElse(null);
	}

}
