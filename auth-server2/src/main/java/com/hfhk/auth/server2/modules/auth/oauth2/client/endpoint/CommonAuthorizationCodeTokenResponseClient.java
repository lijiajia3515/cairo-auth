package com.hfhk.auth.server2.modules.auth.oauth2.client.endpoint;

import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommonAuthorizationCodeTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
	private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> DEFAULT = new DefaultAuthorizationCodeTokenResponseClient();
	private final Map<String, OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>> client = new ConcurrentHashMap<>();

	public CommonAuthorizationCodeTokenResponseClient() {
		WechatWebOAuth2AuthorizationCodeTokenResponseClient wechatWeb = new WechatWebOAuth2AuthorizationCodeTokenResponseClient();
		client.put("wechat-web", wechatWeb);
		client.put("wechat-open", wechatWeb);
	}

	@Override
	public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
		return client.getOrDefault(authorizationGrantRequest.getClientRegistration().getRegistrationId(), DEFAULT).getTokenResponse(authorizationGrantRequest);
	}
}
