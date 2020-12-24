package com.hfhk.auth.service.config;


import com.hfhk.auth.service.security.oauth2.server.resource.authentication.HfhkJwtAuthenticationConverter;
import com.hfhk.cairo.security.authentication.RemoteUser;
import com.hfhk.cairo.security.oauth2.server.resource.web.CairoBearerTokenAccessDeniedHandler;
import com.hfhk.cairo.security.oauth2.server.resource.web.CairoBearerTokenAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

	@Bean
	public HfhkJwtAuthenticationConverter jwtAuthenticationConverter(MongoTemplate mongoTemplate,
																	 RedisTemplate<String, RemoteUser> redisTemplate) {
		return new HfhkJwtAuthenticationConverter(mongoTemplate, redisTemplate);
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http,
											CairoBearerTokenAuthenticationEntryPoint entryPoint,
											CairoBearerTokenAccessDeniedHandler accessDeniedHandler,
											HfhkJwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests(authorizeRequests -> authorizeRequests
				.mvcMatchers("/test/**").permitAll()
				.mvcMatchers("/**").authenticated()
			)
			.oauth2ResourceServer()
			.authenticationEntryPoint(entryPoint)
			.accessDeniedHandler(accessDeniedHandler)
			.jwt()
			.jwtAuthenticationConverter(jwtAuthenticationConverter)
			.and()
			.and()
			.sessionManagement()
			.sessionAuthenticationStrategy(new SessionFixationProtectionStrategy())
			.enableSessionUrlRewriting(true)
			.and()
			.exceptionHandling()
			.and()
		;
		return http.build();
	}

	// @Bean
	// public SecurityExpressionHandler<FilterInvocation> HfhkWebSecurityExpressionHandler() {
	// 	return new CairoWebSecurityExpressionHandler();
	// }
	//
	// @Bean
	// public MethodSecurityExpressionHandler HfhkMethodSecurityExpressionHandler() {
	// 	return new CairoMethodSecurityExpressionHandler();
	// }

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> {
			web.ignoring().antMatchers("/webjars/**");
		};
	}


	@Bean
	public PasswordEncoder hfhkPasswordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
