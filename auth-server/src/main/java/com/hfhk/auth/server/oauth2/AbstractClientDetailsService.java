package com.hfhk.auth.server.oauth2;

import com.hfhk.auth.domain.mongo.ClientV2Mongo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractClientDetailsService {
	protected final MongoTemplate mongoTemplate;

	protected AbstractClientDetailsService(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	protected BaseClientDetails buildClient(ClientV2Mongo client) {
		BaseClientDetails baseClientDetails = new BaseClientDetails(
			client.getClientId(),
			String.join(",", Optional.ofNullable(client.getResourceIds()).orElse(Collections.emptyList())),
			Optional.ofNullable(client.getScopes()).orElse(Collections.emptyList()).stream().map(ClientV2Mongo.Scope::getName).collect(Collectors.joining(",")),
			String.join(",", Optional.ofNullable(client.getGrantTypes()).orElse(Collections.emptyList())),
			String.join(",", Optional.ofNullable(client.getAuthorities()).orElse(Collections.emptyList())),
			String.join(",", Optional.ofNullable(client.getRedirectUris()).orElse(Collections.emptyList()))
		);
		baseClientDetails.setClientSecret(client.getClientSecret());
		baseClientDetails.setAutoApproveScopes(
			Optional.ofNullable(client.getScopes())
				.orElse(Collections.emptyList())
				.stream()
				.filter(ClientV2Mongo.Scope::isAutoApprove)
				.map(ClientV2Mongo.Scope::getName).collect(Collectors.toList())
		);
		return baseClientDetails;
	}
}
