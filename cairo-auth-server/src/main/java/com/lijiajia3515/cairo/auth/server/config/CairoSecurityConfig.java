package com.lijiajia3515.cairo.auth.server.config;

import com.lijiajia3515.cairo.auth.server.modules.auth.CairoAuthSuccessHandler;
import com.lijiajia3515.cairo.auth.server.modules.auth.CairoOAuth2UserService;
import com.lijiajia3515.cairo.auth.server.modules.auth.CairoUserService;
import com.lijiajia3515.cairo.auth.server.modules.auth.oauth2.client.endpoint.CommonAuthorizationCodeTokenResponseClient;
import com.lijiajia3515.cairo.auth.server.security.oauth2.server.resource.authentication.CairoJwtAuthenticationConverter;
import com.lijiajia3515.cairo.security.authentication.RemoteUser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class CairoSecurityConfig {

	@Configuration
	public static class SecurityOAuth2Config {
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

		@Bean
		@Primary
		public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter(MongoTemplate mongoTemplate, RedisTemplate<String, RemoteUser> redisTemplate) {
			return new CairoJwtAuthenticationConverter(mongoTemplate, redisTemplate);
		}
	}

	@Bean
	@Primary
	CairoUserService cairoUserServices(MongoTemplate mongoTemplate) {
		return new CairoUserService(mongoTemplate);
	}

	@Bean
	CairoOAuth2UserService cairoOAuth2UserService(MongoTemplate mongoTemplate) {
		return new CairoOAuth2UserService(mongoTemplate);
	}

	@Bean
	PasswordEncoder cairoPasswordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http,
											CairoUserService hfhkUserService,
											CairoOAuth2UserService oAuth2UserService,
											CairoJwtAuthenticationConverter jwtAuthenticationConverter,
											CairoAuthSuccessHandler successHandler) throws Exception {
		http
			//.csrf().disable()
			.authorizeRequests(requests -> requests
				.mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.mvcMatchers("/actuator/**").permitAll()
				.mvcMatchers("/test/**").permitAll()
				.mvcMatchers("/authentication").permitAll()
				.mvcMatchers("/**").authenticated()
			)
			.formLogin(config -> config.loginPage("/login")
				.permitAll()
				.successHandler(successHandler))
			.oauth2Login(x ->
				x.loginPage("/login")
					.permitAll()
					.tokenEndpoint(tokenEndpointConfig ->
						tokenEndpointConfig.accessTokenResponseClient(new CommonAuthorizationCodeTokenResponseClient()))
					.userInfoEndpoint(endpointConfig ->
						endpointConfig
							.userService(oAuth2UserService))
			)
			.oauth2ResourceServer(config -> {
				config.jwt()
					.jwtAuthenticationConverter(jwtAuthenticationConverter);
			})
			.logout(config ->
				config.logoutSuccessUrl("/")
					.permitAll())
			.rememberMe(config ->
				config.userDetailsService(hfhkUserService))
			.exceptionHandling(config -> {

			});

		return http.build();
	}
}
