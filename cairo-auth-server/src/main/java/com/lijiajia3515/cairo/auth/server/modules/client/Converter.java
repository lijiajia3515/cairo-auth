package com.lijiajia3515.cairo.auth.server.modules.client;

import com.lijiajia3515.auth.domain.mongo.ClientMongo;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class Converter {
	public static RegisteredClient convert(ClientMongo client) {
		return RegisteredClient.withId(client.getClientId())
			.clientId(client.getClientId())
			.clientSecret(client.getClientSecret())
			.clientAuthenticationMethods(clientAuthenticationMethods ->
				Optional.ofNullable(client.getClientAuthenticationMethods())
					.ifPresent(methods ->
						clientAuthenticationMethods.addAll(
							methods.stream()
								.map(ClientAuthenticationMethod::new)
								.collect(Collectors.toSet())
						)
					)
			)
			.authorizationGrantTypes(authorizationGrantTypes ->
				Optional.ofNullable(client.getAuthorizationGrantTypes())
					.ifPresent(types ->
						authorizationGrantTypes.addAll(
							types.stream()
								.map(AuthorizationGrantType::new)
								.collect(Collectors.toSet())
						)
					)
			)
			.scopes(scopes -> Optional.ofNullable(client.getScopes()).ifPresent(scopes::addAll))
			.redirectUris(redirectUris -> Optional.ofNullable(client.getRedirectUris()).ifPresent(redirectUris::addAll))
			.clientSettings(clientSettings ->
				Optional.ofNullable(client.getClientSettings())
					.ifPresent(x ->
						clientSettings.requireProofKey(x.getRequireProofKey())
							.requireUserConsent(x.getRequireUserConsent())
					)
			)
			.tokenSettings(tokenSettings ->
				Optional.ofNullable(client.getTokenSettings())
					.ifPresent(x ->
						tokenSettings.accessTokenTimeToLive(x.getAccessTokenTimeToLive())
							.reuseRefreshTokens(x.getReuseRefreshTokens())
							.refreshTokenTimeToLive(x.getRefreshTokenTimeToLive())
					)
			)
			.build();
	}

	public static ClientMongo convert(RegisteredClient client) {
		return ClientMongo.builder()
			.id(client.getId())
			.clientId(client.getClientId())
			.clientSecret(client.getClientSecret())
			.clientAuthenticationMethods(
				Optional.ofNullable(client.getClientAuthenticationMethods()).orElse(Collections.emptySet())
					.parallelStream()
					.map(ClientAuthenticationMethod::getValue)
					.collect(Collectors.toSet())
			)
			.authorizationGrantTypes(
				Optional.ofNullable(client.getAuthorizationGrantTypes()).orElse(Collections.emptySet())
					.stream()
					.map(AuthorizationGrantType::getValue)
					.collect(Collectors.toSet())
			)
			.scopes(Optional.ofNullable(client.getScopes()).orElse(Collections.emptySet()))
			.redirectUris(Optional.ofNullable(client.getRedirectUris()).orElse(Collections.emptySet()))
			.clientSettings(
				Optional.ofNullable(client.getClientSettings())
					.map(x ->
						ClientMongo.ClientSettings.builder()
							.requireProofKey(x.requireProofKey())
							.requireUserConsent(x.requireUserConsent())
							.build()
					).orElse(new ClientMongo.ClientSettings())
			)
			.tokenSettings(
				Optional.ofNullable(client.getTokenSettings())
					.map(x ->
						ClientMongo.TokenSettings.builder()
							.accessTokenTimeToLive(x.accessTokenTimeToLive())
							.reuseRefreshTokens(x.reuseRefreshTokens())
							.refreshTokenTimeToLive(x.refreshTokenTimeToLive())
							.build()
					).orElse(new ClientMongo.TokenSettings())
			)
			.build();
	}
}
