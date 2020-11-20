package com.hfhk.cairo.auth.security.oauth2;

import com.hfhk.cairo.auth.security.domain.mongo.ClientMongo;
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

	protected BaseClientDetails buildClient(ClientMongo client) {
		BaseClientDetails baseClientDetails = new BaseClientDetails(
			client.getClientId(),
			String.join(",", Optional.ofNullable(client.getResourceIds()).orElse(Collections.emptyList())),
			Optional.ofNullable(client.getScopes()).orElse(Collections.emptyList()).stream().map(ClientMongo.Scope::getName).collect(Collectors.joining(",")),
			String.join(",", Optional.ofNullable(client.getGrantTypes()).orElse(Collections.emptyList())),
			String.join(",", Optional.ofNullable(client.getAuthorities()).orElse(Collections.emptyList())),
			String.join(",", Optional.ofNullable(client.getRedirectUris()).orElse(Collections.emptyList()))
		);
		baseClientDetails.setClientSecret(client.getClientSecret());
		baseClientDetails.setAutoApproveScopes(
			Optional.ofNullable(client.getScopes())
				.orElse(Collections.emptyList())
				.stream()
				.filter(ClientMongo.Scope::isAutoApprove)
				.map(ClientMongo.Scope::getName).collect(Collectors.toList())
		);
		return baseClientDetails;
	}
}
