package com.hfhk.auth.server2.modules.auth.oauth2.client.userinfo;

import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

public interface OAuthUserInfoResponseClient<T> {
	T getResponse(OAuth2UserRequest userRequest, RequestEntity<?> request);
}
