package com.lijiajia3515.cairo.auth.server.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import com.lijiajia3515.cairo.auth.server.framework.jackson.AuthorizationGrantTypeDeserializer;
import com.lijiajia3515.cairo.auth.server.framework.jackson.AuthorizationGrantTypeSerializer;
import com.lijiajia3515.cairo.auth.server.framework.redis.AuthorizationGrantTypeRedisSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

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
	public RedisSerializer<?> objectValueRedisSerializer(AuthorizationGrantTypeRedisSerializer authorizationGrantTypeRedisSerializer) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		SimpleModule module = new SimpleModule()
			.addSerializer(AuthorizationGrantType.class, new AuthorizationGrantTypeSerializer())
			.addDeserializer(AuthorizationGrantType.class, new AuthorizationGrantTypeDeserializer());

		mapper.registerModule(module);
		mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
		GenericJackson2JsonRedisSerializer redisSerializer = new GenericJackson2JsonRedisSerializer(mapper);
		return redisSerializer;
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

	@Bean
	public AuthorizationGrantTypeRedisSerializer authorizationGrantTypeRedisSerializer() {
		return new AuthorizationGrantTypeRedisSerializer();
	}


}
