package com.hfhk.auth.server2.config;

import com.hfhk.auth.server2.modules.user.HfhkUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class DefaultSecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests(authorizeRequests -> authorizeRequests
				.mvcMatchers("/actuator/**").permitAll()
				.mvcMatchers("/test/**").permitAll()
				.mvcMatchers("/**").authenticated()
			)
			.formLogin()
			.and()
			//.oauth2Login()
			//.and()
			//.oauth2Client()
			//.and()
			.sessionManagement()
			.enableSessionUrlRewriting(true)
			.and()
			.exceptionHandling()
			.and();
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

	@Bean
	PasswordEncoder hfhkPasswordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	HfhkUserService users(MongoTemplate mongoTemplate) {
		return new HfhkUserService(mongoTemplate);
	}

}
