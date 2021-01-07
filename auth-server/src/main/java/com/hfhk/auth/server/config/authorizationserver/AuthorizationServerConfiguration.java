package com.hfhk.auth.server.config.authorizationserver;

import com.hfhk.auth.server.oauth2.MyClientDetailsService;
import com.hfhk.auth.server.oauth2.provider.token.CairoUserAuthenticationConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.util.Assert;

import java.security.KeyPair;
import java.util.Optional;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration {

	@Bean
	public KeyPair jwtKeyPair(JwtProperties properties, ApplicationContext context) {
		Assert.notNull(properties.getKeyStore(), "keyStore cannot be null");
		Assert.notNull(properties.getKeyStorePassword(), "keyStorePassword cannot be null");
		Assert.notNull(properties, "keyAlias cannot be null");

		Resource keyStore = context.getResource(properties.getKeyStore());
		char[] keyStorePassword = properties.getKeyStorePassword().toCharArray();
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keyStore, keyStorePassword);

		String keyAlias = properties.getKeyAlias();
		char[] keyPassword = Optional.ofNullable(properties.getKeyPassword()).map(String::toCharArray).orElse(keyStorePassword);
		return keyStoreKeyFactory.getKeyPair(keyAlias, keyPassword);
	}

	@Bean
	public JwtAccessTokenConverter myAccessTokenConverter(KeyPair jwtKeyPair) {
		CairoUserAuthenticationConverter userAuthenticationConverter = new CairoUserAuthenticationConverter();
		userAuthenticationConverter.setDefaultAuthorities("default");
		DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
		accessTokenConverter.setUserTokenConverter(userAuthenticationConverter);

		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setAccessTokenConverter(accessTokenConverter);
		converter.setKeyPair(jwtKeyPair);

		return converter;
	}

	@Bean
	public TokenStore myTokenStore(@Qualifier("myAccessTokenConverter") JwtAccessTokenConverter jwtAccessTokenConverter) {
		return new JwtTokenStore(jwtAccessTokenConverter);
	}

	@Configuration
	public static class AuthConfig extends AuthorizationServerConfigurerAdapter {
		private final MongoTemplate mongoTemplate;
		private final AuthenticationManager authenticationManager;
		private final AccessTokenConverter accessTokenConverter;
		private final TokenStore tokenStore;

		public AuthConfig(MongoTemplate mongoTemplate,
						  AuthenticationManager authenticationManager,
						  @Qualifier("myAccessTokenConverter") AccessTokenConverter accessTokenConverter,
						  @Qualifier("myTokenStore") TokenStore tokenStore) {
			this.mongoTemplate = mongoTemplate;
			this.authenticationManager = authenticationManager;
			this.accessTokenConverter = accessTokenConverter;
			this.tokenStore = tokenStore;
		}

		@Override
		public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
			security.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()")
				.realm("harpoon/oauth/client")
				.allowFormAuthenticationForClients();
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.withClientDetails(myClientDetailsService(mongoTemplate));
		}

		public ClientDetailsService myClientDetailsService(MongoTemplate mongoTemplate) {
			return new MyClientDetailsService(mongoTemplate);
		}

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.authenticationManager(authenticationManager)
				.accessTokenConverter(accessTokenConverter)
				.tokenStore(this.tokenStore)
			;
		}


	}
}
