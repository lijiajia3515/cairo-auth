package com.lijiajia3515.cairo.auth.server.modules.auth;

import com.lijiajia3515.cairo.auth.server.framework.security.core.userdetails.AuthUser;
import com.lijiajia3515.cairo.auth.server.framework.security.core.userdetails.CairoUserService;
import com.lijiajia3515.cairo.core.exception.BusinessException;
import com.lijiajia3515.cairo.security.AuthBusiness;
import com.lijiajia3515.cairo.security.exception.ClientRequiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
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

@RestController
@RequestMapping("/oauth2")
public class OAuthApi {
	private final RegisteredClientRepository registeredClientRepository;
	private final CairoUserService cairoUserService;
	private final PasswordEncoder passwordEncoder;
	private final OAuth2AuthorizationService authorizationService;
	private final StringKeyGenerator codeGenerator = new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);


	public OAuthApi(RegisteredClientRepository registeredClientRepository, CairoUserService cairoUserService,
					PasswordEncoder passwordEncoder,
					OAuth2AuthorizationService authorizationService) {
		this.registeredClientRepository = registeredClientRepository;
		this.passwordEncoder = passwordEncoder;
		this.cairoUserService = cairoUserService;
		this.authorizationService = authorizationService;
	}

	@PostMapping("/password_code")
	@PermitAll
	public Object passwordToken(@RequestBody PasswordParam param) {
		try {
			AuthUser authUser = cairoUserService.loadUserByUsername(param.getUsername());

			if (!passwordEncoder.matches(param.getPassword(), authUser.getPassword())) {
				throw new BusinessException(AuthBusiness.AccessBad);
			}
			UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(authUser, "N/A", authUser.getAuthorities());

			Instant issuedAt = Instant.now();
			Instant expiresAt = issuedAt.plus(60, ChronoUnit.MINUTES);
			OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(
				this.codeGenerator.generateKey(), issuedAt, expiresAt);

			RegisteredClient client = registeredClientRepository.findByClientId(param.getClientId());

			if (client == null) {
				throw new ClientRequiredException(param.getClientId());
			}

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
		} catch (UsernameNotFoundException e) {
			throw new BusinessException(AuthBusiness.Bad);
		}

	}

}
