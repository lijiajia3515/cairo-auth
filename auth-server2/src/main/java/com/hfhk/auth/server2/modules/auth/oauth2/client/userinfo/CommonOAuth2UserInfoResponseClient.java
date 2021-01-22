package com.hfhk.auth.server2.modules.auth.oauth2.client.userinfo;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.HashMap;
import java.util.Map;

public class CommonOAuth2UserInfoResponseClient implements OAuth2UserInfoResponseClient {
	private final OAuth2UserInfoResponseClient DEFAULT = new DefaultOAuth2UserInfoResponseClient();
	private final Map<String, OAuth2UserInfoResponseClient> clients = new HashMap<>();

	public CommonOAuth2UserInfoResponseClient(){
		clients.put("wechat-web",new WechatWebOAuth2UserInfoResponseClient());
	}
	@Override
	public ResponseEntity<Map<String, Object>> getResponse(OAuth2UserRequest userRequest, RequestEntity<?> request) {
		return clients.getOrDefault(userRequest.getClientRegistration().getRegistrationId(), DEFAULT).getResponse(userRequest, request);
	}
}
