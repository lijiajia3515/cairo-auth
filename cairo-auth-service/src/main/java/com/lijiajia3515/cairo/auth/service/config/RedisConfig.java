package com.lijiajia3515.cairo.auth.service.config;

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
		template.afterPropertiesSet();
		return template;
	}
}
