package com.hfhk.cairo.auth.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hfhk.cairo.security.authentication.RemoteUser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {
	@Bean("keyRedisSerializer")
	public RedisSerializer<?> keyRedisSerializer() {
		return RedisSerializer.string();
	}

	@Bean("stringValueRedisSerializer")
	public RedisSerializer<?> stringValueRedisSerializer() {
		return RedisSerializer.string();
	}

	@Bean("objectValueRedisSerializer")
	public RedisSerializer<?> objectValueRedisSerializer() {
		final ObjectMapper objectMapper = new ObjectMapper();
		return new GenericJackson2JsonRedisSerializer();
	}

	@Bean
	RedisTemplate<String, Object> stringObjectRedisTemplate(
		RedisConnectionFactory factory,
		@Qualifier("keyRedisSerializer") RedisSerializer<?> keyRedisSerializer,
		@Qualifier("objectValueRedisSerializer") RedisSerializer<?> valueRedisSerializer) {
		final RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);

		template.setKeySerializer(keyRedisSerializer);
		template.setHashKeySerializer(keyRedisSerializer);

		template.setValueSerializer(valueRedisSerializer);
		template.setHashValueSerializer(valueRedisSerializer);

		return template;
	}

	@Bean
	RedisTemplate<String, RemoteUser> stringRemoteUserRedisTemplate(
		RedisConnectionFactory factory,
		@Qualifier("keyRedisSerializer") RedisSerializer<?> keyRedisSerializer,
		@Qualifier("objectValueRedisSerializer") RedisSerializer<?> valueRedisSerializer) {
		final RedisTemplate<String, RemoteUser> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);

		template.setKeySerializer(keyRedisSerializer);
		template.setHashKeySerializer(keyRedisSerializer);

		template.setValueSerializer(valueRedisSerializer);
		template.setHashValueSerializer(valueRedisSerializer);

		return template;
	}
}
