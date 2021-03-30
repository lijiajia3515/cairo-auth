package com.lijiajia3515.cairo.auth.server.config;

import com.lijiajia3515.cairo.auth.server.framework.security.web.authentication.CairoAuthSuccessHandler;
import com.lijiajia3515.cairo.auth.server.framework.security.oauth2.client.userinfo.CairoOAuth2UserService;
import com.lijiajia3515.cairo.auth.server.framework.security.core.userdetails.CairoUserService;
import com.lijiajia3515.cairo.auth.server.framework.security.oauth2.core.http.converter.CommonAuthorizationCodeTokenResponseClient;
import com.lijiajia3515.cairo.auth.server.framework.security.oauth2.resourceserver.jwt.authentication.CairoJwtAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

//	@Bean
//	RedisTemplate<String, Object> stringRemoteUserRedisTemplate(
//		RedisConnectionFactory factory,
//		@Qualifier("keyRedisSerializer") RedisSerializer<?> keyRedisSerializer,
//		@Qualifier("objectValueRedisSerializer") RedisSerializer<?> valueRedisSerializer) {
//		final RedisTemplate<String, Object> template = new RedisTemplate<>();
//		template.setConnectionFactory(factory);
//
//		template.setKeySerializer(keyRedisSerializer);
//		template.setHashKeySerializer(keyRedisSerializer);
//
//		template.setValueSerializer(valueRedisSerializer);
//		template.setHashValueSerializer(valueRedisSerializer);
//
//		template.afterPropertiesSet();
//		return template;
//	}

	@Bean
	@Primary
	public CairoJwtAuthenticationConverter jwtAuthenticationConverter(MongoTemplate mongoTemplate, RedisTemplate<String, Object> redisTemplate) {
		return new CairoJwtAuthenticationConverter(mongoTemplate, redisTemplate);
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
											CairoUserService cairoUserService,
											CairoOAuth2UserService oAuth2UserService,
											CairoJwtAuthenticationConverter jwtAuthenticationConverter,
											CairoAuthSuccessHandler successHandler) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests(requests -> requests
				.mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.mvcMatchers("/actuator/**").permitAll()
				.mvcMatchers("/oauth2/**").permitAll()
				.mvcMatchers("/oauth2/password_code").permitAll()
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
				config.userDetailsService(cairoUserService))
			.exceptionHandling(config -> {

			});

		return http.build();
	}
}
