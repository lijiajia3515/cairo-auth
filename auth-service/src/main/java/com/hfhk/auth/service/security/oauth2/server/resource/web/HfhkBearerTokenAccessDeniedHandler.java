package com.hfhk.auth.service.security.oauth2.server.resource.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import com.hfhk.cairo.core.result.BusinessResult;
import com.hfhk.cairo.core.result.Result;
import com.hfhk.cairo.security.status.AuthBusiness;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class HfhkBearerTokenAccessDeniedHandler implements AccessDeniedHandler {
	@Getter
	@Setter
	@Accessors(fluent = true)
	private ObjectMapper objectMapper;

	@Getter
	@Setter
	@Accessors(fluent = true)
	private String realmName;

	/**
	 * Collect error details from the provided parameters and format according to RFC
	 * 6750, specifically {@code error}, {@code error_description}, {@code error_uri}, and
	 * {@code scope}.
	 *
	 * @param request               that resulted in an <code>AccessDeniedException</code>
	 * @param response              so that the user agent can be advised of the failure
	 * @param accessDeniedException that caused the invocation
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
					   AccessDeniedException accessDeniedException) {
		Map<String, String> parameters = new LinkedHashMap<>();
		if (this.realmName != null) {
			parameters.put("realm", this.realmName);
		}
		if (request.getUserPrincipal() instanceof AbstractOAuth2TokenAuthenticationToken) {
			parameters.put("error", BearerTokenErrorCodes.INSUFFICIENT_SCOPE);
			parameters.put("error_description",
				"The request requires higher privileges than provided by the access token.");
			parameters.put("error_uri", "https://tools.ietf.org/html/rfc6750#section-3.1");
		}
		String wwwAuthenticate = computeWWWAuthenticateHeaderValue(parameters);
		response.addHeader(HttpHeaders.WWW_AUTHENTICATE, wwwAuthenticate);
		response.setStatus(HttpStatus.FORBIDDEN.value());

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		Result<String> returnValue = BusinessResult.build(AuthBusiness.AccessDenied,accessDeniedException.getMessage());
		try {
			response.getWriter().write(objectMapper.writeValueAsString(returnValue));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String computeWWWAuthenticateHeaderValue(Map<String, String> parameters) {
		StringBuilder wwwAuthenticate = new StringBuilder();
		wwwAuthenticate.append("Bearer");
		if (!parameters.isEmpty()) {
			wwwAuthenticate.append(" ");
			int i = 0;
			for (Map.Entry<String, String> entry : parameters.entrySet()) {
				wwwAuthenticate.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
				if (i != parameters.size() - 1) {
					wwwAuthenticate.append(", ");
				}
				i++;
			}
		}
		return wwwAuthenticate.toString();
	}

}
