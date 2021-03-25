package com.lijiajia3515.cairo.auth.server.config;

import cn.hutool.core.codec.Base64Decoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiajia3515.cairo.auth.server.framework.security.oauth2.OAuth2Properties;
import com.lijiajia3515.cairo.auth.server.framework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import com.lijiajia3515.cairo.auth.server.framework.security.oauth2.resourceserver.jwt.jose.Jwks;
import com.lijiajia3515.cairo.auth.server.framework.security.oauth2.server.authorization.RedisOAuth2AuthorizationService;
import com.lijiajia3515.cairo.auth.server.framework.security.oauth2.server.authorization.client.CairoRegisteredClientRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;


@Slf4j
@Configuration(proxyBeanMethods = false)
@Import(OAuth2AuthorizationServerConfiguration.class)
public class AuthorizationServerConfig {

	@Bean
	public RegisteredClientRepository registeredClientRepository(MongoTemplate mongoTemplate) {
		return new CairoRegisteredClientRepository(mongoTemplate);
	}

	@Bean
	public OAuth2AuthorizationService oAuth2AuthorizationService(RedisConnectionFactory factory) {
		RedisOAuth2AuthorizationService oAuth2AuthorizationService = new RedisOAuth2AuthorizationService(factory);
		oAuth2AuthorizationService.setNamespace("auth-server:authorization");
		return oAuth2AuthorizationService;
	}


	@Bean
	public HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenResponseHttpMessageConverter(ObjectMapper objectMapper) {
		return new OAuth2AccessTokenResponseHttpMessageConverter(objectMapper);
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource(List<JWK> JWKs) {
		JWKSet jwkSet = new JWKSet(JWKs);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	@Bean
	public JwtDecoder cairoJwtDecoder(List<JWK> JWKs) {
		return JWKs.stream()
			.findFirst()
			.flatMap(jwk -> {
				try {
					return Optional.of((RSAPublicKey) jwk.toPublicJWK().toRSAKey().toPublicKey());
				} catch (JOSEException e) {
					e.printStackTrace();
					return Optional.empty();
				}
			})
			.map(publicKey -> NimbusJwtDecoder.withPublicKey(publicKey).build())
			.orElseThrow();
	}

	@Bean
	List<JWK> oauth2JWKs(OAuth2Properties properties) throws NoSuchAlgorithmException, InvalidKeySpecException {
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64Decoder.decode(properties.getJwk().getPublicKey().getBytes()));
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64Decoder.decode(properties.getJwk().getPrivateKey().getBytes()));
		KeyFactory instance = KeyFactory.getInstance("RSA");
		RSAPublicKey publicKey = (RSAPublicKey) instance.generatePublic(publicKeySpec);
		RSAPrivateKey privateKey = (RSAPrivateKey) instance.generatePrivate(privateKeySpec);
		return Collections.singletonList(new RSAKey.Builder(publicKey).keyID(properties.getJwk().getId()).privateKey(privateKey).build());
	}

	// @Bean
	// @ConditionalOnMissingBean
	List<JWK> defaultJWKs() {
		RSAKey key = Jwks.generateRsa("1");
		return Collections.singletonList(key);
	}

	@Bean
	public ProviderSettings providerSettings(OAuth2Properties properties) {
		return new ProviderSettings().issuer(properties.getIssuer());
	}

}
