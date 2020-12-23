package com.hfhk.auth.service.security.oauth2.server.resource.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hfhk.cairo.core.business.Business;
import com.hfhk.cairo.core.result.BusinessResult;
import com.hfhk.cairo.core.result.Result;
import com.hfhk.cairo.security.status.AuthBusiness;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class HfhkBearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Getter
	@Setter
	@Accessors(fluent = true)
	private ObjectMapper objectMapper = new ObjectMapper();

	@Getter
	@Setter
	@Accessors(fluent = true)
	private String realmName;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
		Business status = AuthBusiness.AccessRequired;
		Map<String, String> parameters = new LinkedHashMap<>();
		if (this.realmName != null) {
			parameters.put("realm", this.realmName);
		}

		if (authException instanceof OAuth2AuthenticationException) {
			OAuth2Error error = ((OAuth2AuthenticationException) authException).getError();
			parameters.put("error", error.getErrorCode());
			if (StringUtils.hasText(error.getDescription())) {
				parameters.put("error_description", error.getDescription());
			}
			if (StringUtils.hasText(error.getUri())) {
				parameters.put("error_uri", error.getUri());
			}
			if (error instanceof BearerTokenError) {
				BearerTokenError bearerTokenError = (BearerTokenError) error;
				if (StringUtils.hasText(bearerTokenError.getScope())) {
					parameters.put("scope", bearerTokenError.getScope());
				}
				httpStatus = ((BearerTokenError) error).getHttpStatus();
			}
			if (authException instanceof InvalidBearerTokenException) {
				status = AuthBusiness.AccessBad;
			}
		}
		String wwwAuthenticate = computeWWWAuthenticateHeaderValue(parameters);
		response.addHeader(HttpHeaders.WWW_AUTHENTICATE, wwwAuthenticate);
		response.setStatus(httpStatus.value());

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		Result<String> build = BusinessResult.build(status, authException.getMessage());
		try {
			response.getWriter().write(objectMapper.writeValueAsString(build));
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
