package com.hfhk.auth.server2.config;

import com.hfhk.auth.domain.mongo.Mongo;
import com.hfhk.auth.server2.modules.auth.HfhkAuthSuccessHandler;
import com.hfhk.auth.server2.modules.auth.HfhkOAuth2UserService;
import com.hfhk.auth.server2.modules.auth.HfhkUserService;
import com.hfhk.auth.server2.modules.auth.oauth2.client.endpoint.CommonAuthorizationCodeTokenResponseClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class DefaultSecurityConfig {
	@Bean
	@Primary
	HfhkUserService hfhkUserServices(MongoTemplate mongoTemplate) {
		return new HfhkUserService(mongoTemplate);
	}

	@Bean
	public HfhkOAuth2UserService hfhkOAuth2UserService(MongoTemplate mongoTemplate) {
		return new HfhkOAuth2UserService(mongoTemplate);
	}

	public

	@Bean
	PasswordEncoder hfhkPasswordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http,
											HfhkUserService hfhkUserService,
											HfhkOAuth2UserService hfhkOAuth2UserService,
											HfhkAuthSuccessHandler successHandler) throws Exception {
		http
			//.csrf().disable()
			.authorizeRequests(requests -> requests
				.mvcMatchers("/actuator/**").permitAll()
				.mvcMatchers("/test/**").permitAll()
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
							.userService(hfhkOAuth2UserService)))
			.logout(config ->
				config.logoutSuccessUrl("/")
					.permitAll())
			.rememberMe(config ->
				config.userDetailsService(hfhkUserService))
			.exceptionHandling(config -> {

			});

		return http.build();
	}

	//@Bean
	//UserDetailsService users() {
	//	UserDetails user = User.withDefaultPasswordEncoder()
	//		.username("root")
	//		.password("root")
	//		.roles("USER")
	//		.build();
	//	return new InMemoryUserDetailsManager(user);
	//}


}
