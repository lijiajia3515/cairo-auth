package com.hfhk.auth.server2.modules.auth.oauth2.client.userinfo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommonOAuth2UserRequestEntityConverter implements Converter<OAuth2UserRequest, RequestEntity<?>> {
	private final OAuthUserRequestEntityConverter DEFAULT = new OAuthUserRequestEntityConverter();
	private final Map<String, Converter<OAuth2UserRequest, RequestEntity<?>>> converters = new ConcurrentHashMap<>();

	public CommonOAuth2UserRequestEntityConverter() {
		converters.put("wechat-web", new WechatWebOAuth2UserRequestEntityConverter());
	}

	@Override
	public RequestEntity<?> convert(OAuth2UserRequest source) {
		return converters.getOrDefault(source.getClientRegistration().getRegistrationId(), DEFAULT).convert(source);
	}
}
