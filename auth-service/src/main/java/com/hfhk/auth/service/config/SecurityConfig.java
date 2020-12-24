package com.hfhk.auth.service.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hfhk.auth.service.authentication.HfhkJwtAuthenticationConverter;
import com.hfhk.auth.service.security.oauth2.server.resource.web.HfhkBearerTokenAccessDeniedHandler;
import com.hfhk.auth.service.security.oauth2.server.resource.web.HfhkBearerTokenAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

	@Bean
	public HfhkBearerTokenAuthenticationEntryPoint hfhkBearerTokenAuthenticationEntryPoint(ObjectMapper objectMapper) {
		return new HfhkBearerTokenAuthenticationEntryPoint()
			.realmName("hfhk")
			.objectMapper(objectMapper);
	}

	@Bean
	public HfhkBearerTokenAccessDeniedHandler hfhkBearerTokenAccessDeniedHandler(ObjectMapper objectMapper) {
		return new HfhkBearerTokenAccessDeniedHandler()
			.realmName("hfhk")
			.objectMapper(objectMapper);
	}


	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http,
											HfhkBearerTokenAuthenticationEntryPoint entryPoint,
											HfhkBearerTokenAccessDeniedHandler accessDeniedHandler,
											HfhkJwtAuthenticationConverter jwtAuthenticationConverter
											) throws Exception {
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

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/webjars/**");
	}


	//@Bean
	//public SecurityExpressionHandler<FilterInvocation> HfhkWebSecurityExpressionHandler() {
	//	return new CairoWebSecurityExpressionHandler();
	//}
	//
	//@Bean
	//public MethodSecurityExpressionHandler HfhkMethodSecurityExpressionHandler() {
	//	return new CairoMethodSecurityExpressionHandler();
	//}

	@Bean
	public PasswordEncoder hfhkPasswordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public BearerTokenResolver hfhkBearerTokenResolver() {
		DefaultBearerTokenResolver resolver = new DefaultBearerTokenResolver();
		resolver.setAllowFormEncodedBodyParameter(true);
		resolver.setAllowUriQueryParameter(true);
		return resolver;
	}
}
