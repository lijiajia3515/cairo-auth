package com.lijiajia3515.cairo.auth.server.modules.auth.oauth2.client.endpoint;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;

import java.util.*;

public class WechatWebMapOAuth2AccessTokenResponseConverter implements Converter<Map<String, String>, OAuth2AccessTokenResponse> {

	private static final Set<String> TOKEN_RESPONSE_PARAMETER_NAMES = new HashSet<>(
		Arrays.asList(OAuth2ParameterNames.ACCESS_TOKEN, OAuth2ParameterNames.EXPIRES_IN, OAuth2ParameterNames.REFRESH_TOKEN, OAuth2ParameterNames.SCOPE));

	@Override
	public OAuth2AccessTokenResponse convert(Map<String, String> tokenResponseParameters) {
		String accessToken = tokenResponseParameters.get(OAuth2ParameterNames.ACCESS_TOKEN);
		long expiresIn = getExpiresIn(tokenResponseParameters);
		Set<String> scopes = getScopes(tokenResponseParameters);
		String refreshToken = tokenResponseParameters.get(OAuth2ParameterNames.REFRESH_TOKEN);
		Map<String, Object> additionalParameters = new LinkedHashMap<>();
		for (Map.Entry<String, String> entry : tokenResponseParameters.entrySet()) {
			if (!TOKEN_RESPONSE_PARAMETER_NAMES.contains(entry.getKey())) {
				additionalParameters.put(entry.getKey(), entry.getValue());
			}
		}
		// @formatter:off
		return OAuth2AccessTokenResponse.withToken(accessToken)
			.tokenType(OAuth2AccessToken.TokenType.BEARER)
			.expiresIn(expiresIn)
			.scopes(scopes)
			.refreshToken(refreshToken)
			.additionalParameters(additionalParameters)
			.build();
		// @formatter:on
	}

	private OAuth2AccessToken.TokenType getAccessTokenType(Map<String, String> tokenResponseParameters) {
		if (OAuth2AccessToken.TokenType.BEARER.getValue()
			.equalsIgnoreCase(tokenResponseParameters.get(OAuth2ParameterNames.TOKEN_TYPE))) {
			return OAuth2AccessToken.TokenType.BEARER;
		}
		return null;
	}

	private long getExpiresIn(Map<String, String> tokenResponseParameters) {
		if (tokenResponseParameters.containsKey(OAuth2ParameterNames.EXPIRES_IN)) {
			try {
				return Long.parseLong(tokenResponseParameters.get(OAuth2ParameterNames.EXPIRES_IN));
			} catch (NumberFormatException ex) {
			}
		}
		return 0;
	}

	private Set<String> getScopes(Map<String, String> tokenResponseParameters) {
		if (tokenResponseParameters.containsKey(OAuth2ParameterNames.SCOPE)) {
			String scope = tokenResponseParameters.get(OAuth2ParameterNames.SCOPE);
			return new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
		}
		return Collections.emptySet();
	}
}
