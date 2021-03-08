package com.lijiajia3515.cairo.auth.server.modules.client;

import com.lijiajia3515.auth.domain.mongo.ClientMongo;
import com.lijiajia3515.auth.domain.mongo.Mongo;
import com.lijiajia3515.cairo.core.CoreConstants;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Client Service
 */
@Slf4j

@Service
public class ClientService {
	private final MongoTemplate mongoTemplate;

	public ClientService(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public List<RegisteredClient> all() {
		return mongoTemplate.findAll(ClientMongo.class, Mongo.Collection.CLIENT)
			.stream()
			.map(Converter::convert)
			.collect(Collectors.toList());
	}

	@Transactional(rollbackFor = Exception.class)
	public Optional<RegisteredClient> save(RegisteredClient client) {
		return Optional.ofNullable(client)
			.map(Converter::convert)
			.map(x -> x.setId(null).setClientId(CoreConstants.SNOWFLAKE.nextIdStr()).setClientSecret(CoreConstants.SNOWFLAKE.nextIdStr()))
			.map(x -> mongoTemplate.insert(x, Mongo.Collection.CLIENT))
			.map(Converter::convert);
	}

	@Transactional(rollbackFor = Exception.class)
	public Optional<RegisteredClient> save(ClientMongo client) {
		return Optional.ofNullable(client)
			.map(x -> mongoTemplate.insert(x, Mongo.Collection.CLIENT))
			.map(Converter::convert);
	}

	/**
	 * 根据id 更新client属性
	 *
	 * @param client client
	 * @return client
	 */
	@Transactional(rollbackFor = Exception.class)
	public Optional<RegisteredClient> modifyById(RegisteredClient client) {
		Query query = Query.query(Criteria.where(ClientMongo.FIELD._ID));
		Update update = buildClientMongoUpdate(client);
		UpdateResult updateResult = mongoTemplate.updateFirst(query, update, ClientMongo.class, Mongo.Collection.CLIENT);
		log.info("[Client][ModifyById]-> Result: {}", updateResult);
		return Optional.ofNullable(mongoTemplate.findOne(query, ClientMongo.class, Mongo.Collection.CLIENT)).map(Converter::convert);
	}

	/**
	 * 根据ClientId 更新Client属性
	 *
	 * @param client client
	 * @return client
	 */
	@Transactional(rollbackFor = Exception.class)
	public Optional<RegisteredClient> modifyByClientId(RegisteredClient client) {
		Query query = Query.query(Criteria.where(ClientMongo.FIELD.CLIENT_ID));
		Update update = buildClientMongoUpdate(client);
		UpdateResult updateResult = mongoTemplate.updateFirst(query, update, ClientMongo.class, Mongo.Collection.CLIENT);

		log.info("[Client][ModifyByClientId]-> Result: {}", updateResult);
		return Optional.ofNullable(mongoTemplate.findOne(query, ClientMongo.class, Mongo.Collection.CLIENT)).map(Converter::convert);
	}

	/**
	 * 根据ClientId 更新状态
	 *
	 * @param clientId clientId
	 * @param state    state default value is false
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateStatus(String clientId, boolean state) {
		Query query = Query.query(Criteria.where(ClientMongo.FIELD.CLIENT_ID).is(clientId));
		Update update = Update.update(ClientMongo.FIELD.ENABLED, state);
		UpdateResult updateResult = mongoTemplate.updateFirst(query, update, ClientMongo.class, Mongo.Collection.CLIENT);
		log.info("[Client][UpdateStatus]-> Result: {}", updateResult);
	}

	/**
	 * 构建更新条件
	 *
	 * @param client client
	 * @return update
	 */
	private Update buildClientMongoUpdate(RegisteredClient client) {
		Update update = new Update();
		Optional.ofNullable(client.getClientSecret())
			.ifPresent(x -> update.set(ClientMongo.FIELD.CLIENT_SECRET, x));
		Optional.ofNullable(client.getAuthorizationGrantTypes())
			.ifPresent(x ->
				update.set(ClientMongo.FIELD.AUTHORIZATION_GRANT_TYPES,
					x.parallelStream()
						.map(AuthorizationGrantType::getValue)
						.collect(Collectors.toSet())
				)
			);
		Optional.ofNullable(client.getScopes()).ifPresent(x -> update.set(ClientMongo.FIELD.SCOPES, x));
		Optional.ofNullable(client.getClientAuthenticationMethods())
			.ifPresent(x -> update.set(ClientMongo.FIELD.CLIENT_AUTHENTICATION_METHODS,
				x.parallelStream().map(ClientAuthenticationMethod::getValue).collect(Collectors.toSet()))
			);
		Optional.ofNullable(client.getRedirectUris())
			.ifPresent(redirectUris -> update.set(ClientMongo.FIELD.REDIRECT_URIS, redirectUris));
		Optional.ofNullable(client.getClientSettings())
			.ifPresent(clientSettings -> update.set(ClientMongo.FIELD.CLIENT_SETTINGS, clientSettings));
		Optional.ofNullable(client.getTokenSettings())
			.ifPresent(tokenSettings -> update.set(ClientMongo.FIELD.TOKEN_SETTINGS, tokenSettings));
		return update;
	}


}
