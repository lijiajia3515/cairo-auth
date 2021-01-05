package com.hfhk.auth.server2.config;

import com.hfhk.auth.server2.modules.auth.HfhkRegisteredClientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.key.CryptoKeySource;
import org.springframework.security.crypto.key.StaticKeyGeneratingCryptoKeySource;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;


@Configuration(proxyBeanMethods = false)
@Import(OAuth2AuthorizationServerConfiguration.class)
public class AuthorizationServerConfig {

	//@Bean
	//public RegisteredClientRepository registeredClientRepository() {
	//	RegisteredClient dxgxJsjApp = RegisteredClient.withId(UUID.randomUUID().toString())
	//		.clientId("hfhk_dhgxjsj")
	//		.clientSecret("hfhk_dhgxjsj")
	//		.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
	//		.clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
	//		.clientAuthenticationMethod(ClientAuthenticationMethod.POST)
	//		.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
	//		.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
	//		.authorizationGrantType(AuthorizationGrantType.IMPLICIT)
	//		.redirectUri("http://localhost:10000/login/oauth2/code/hfhk-dhgxjsj")
	//		.scope("openid")
	//		.scope("check.read")
	//		.clientSettings(clientSettings -> clientSettings.requireUserConsent(true))
	//		.build();
//
	//	RegisteredClient serviceCommonCheckClient = RegisteredClient.withId(UUID.randomUUID().toString())
	//		.clientId("hfhk_common_check")
	//		.clientSecret("hfhk_common_check")
	//		.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
	//		.clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
	//		.clientAuthenticationMethod(ClientAuthenticationMethod.POST)
	//		.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
	//		.redirectUri("http://localhost:7823/authorized")
	//		.scope("openid")
	//		.scope("check.read")
	//		.clientSettings(clientSettings -> clientSettings.requireUserConsent(true))
	//		.build();
	//	return new InMemoryRegisteredClientRepository(dxgxJsjApp, serviceCommonCheckClient);
	//}

	@Bean
	public RegisteredClientRepository registeredClientRepository(MongoTemplate mongoTemplate) {
		return new HfhkRegisteredClientRepository(mongoTemplate);
	}

	@Bean
	public CryptoKeySource keySource() {
		return new StaticKeyGeneratingCryptoKeySource();
	}

	@Bean
	public ProviderSettings providerSettings() {
		return new ProviderSettings().issuer("http://auth.hfhksoft.com");
	}

}
