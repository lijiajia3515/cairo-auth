package com.hfhk.auth.server2.config;

import com.hfhk.auth.server2.jose.Jwks;
import com.hfhk.auth.server2.modules.auth.HfhkRegisteredClientRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Configuration(proxyBeanMethods = false)
@Import(OAuth2AuthorizationServerConfiguration.class)
public class AuthorizationServerConfig {
	public static final String JWK_KEY_ALL = "JWK:*";

	public String jwkStr(String key) {
		return String.format("JWK:%s", key);
	}

	@Bean
	public RegisteredClientRepository registeredClientRepository(MongoTemplate mongoTemplate) {
		return new HfhkRegisteredClientRepository(mongoTemplate);
	}

	@Bean
	RedisTemplate<String, RSAKey> stringRSAKeyRedisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, RSAKey> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new JdkSerializationRedisSerializer());
		template.setHashValueSerializer(new JdkSerializationRedisSerializer());

		return template;
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource(RedisTemplate<String, RSAKey> template) {
		String dateKey = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
		RSAKey value = template.opsForValue().get(jwkStr(dateKey));
		if (value == null) {
			value = Jwks.generateRsa(dateKey);
			template.opsForValue().set(jwkStr(dateKey), value, -1);
		}
		JWKSet jwkSet = new JWKSet(value);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	@Bean
	public ProviderSettings providerSettings() {
		return new ProviderSettings().issuer("http://auth.hfhksoft.com");
	}

}
