package com.lijiajia3515.cairo.auth.server.modules;

import com.lijiajia3515.cairo.auth.server.modules.auth.AuthUser;
import com.lijiajia3515.cairo.auth.server.modules.auth.CairoUserService;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Collections;

@RequestMapping("/oauth2")
@RestController
public class PasswordApi {
	private final RegisteredClientRepository registeredClientRepository;
	private final CairoUserService cairoUserService;
	private final OAuth2AuthorizationService authorizationService;
	private final StringKeyGenerator codeGenerator = new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);

	public PasswordApi(RegisteredClientRepository registeredClientRepository, CairoUserService cairoUserService, OAuth2AuthorizationService authorizationService) {
		this.registeredClientRepository = registeredClientRepository;
		this.cairoUserService = cairoUserService;
		this.authorizationService = authorizationService;
	}

	@PostMapping("/password_code")
	@PermitAll
	public Object passwordToken(@RequestBody PasswordParam param) {
		AuthUser authUser = cairoUserService.loadUserByUsername(param.getUsername());
		UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(authUser, "N/A", authUser.getAuthorities());

		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plus(60, ChronoUnit.MINUTES);
		OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(
			this.codeGenerator.generateKey(), issuedAt, expiresAt);

		RegisteredClient client = registeredClientRepository.findByClientId(param.getClientId());
		OAuth2AuthorizationRequest request = OAuth2AuthorizationRequest.authorizationCode()
			.clientId(param.getClientId())
			.authorizationUri("http://127.0.0.1/oauth2/authorize")
			.authorizationRequestUri("http://localhost:9000")
			.scopes(param.getScopes())
			.state(param.getState())
			.build();
		OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(client);
		OAuth2Authorization authorization = builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.principalName(principal.getName())
			.token(authorizationCode)
			.attribute(OAuth2Authorization.AUTHORIZED_SCOPE_ATTRIBUTE_NAME, param.getScopes())
			.attribute(Principal.class.getName(), principal)
			.attribute(OAuth2AuthorizationRequest.class.getName(), request)
			.attribute(OAuth2ParameterNames.STATE, request.getState())
			.build();

		authorizationService.save(authorization);
		return authorizationCode;
	}
}
