package com.hfhk.auth.server2.config;

import com.hfhk.auth.server2.modules.auth.HfhkAuthSuccessHandler;
import com.hfhk.auth.server2.modules.auth.HfhkUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class DefaultSecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http,
											@Qualifier("hfhkUserServices") UserDetailsService userDetailsService,
											HfhkAuthSuccessHandler successHandler) throws Exception {
		http
			//.csrf().disable()
			.authorizeRequests(authorizeRequests -> authorizeRequests
				.mvcMatchers("/actuator/**").permitAll()
				.mvcMatchers("/test/**").permitAll()
				.mvcMatchers("/login").permitAll()
				.mvcMatchers("/**").authenticated()
			)
			.formLogin()
			.loginPage("/login")
			.permitAll()
			.successHandler(successHandler)
			.and()
			.logout()
			.logoutSuccessUrl("/")
			.permitAll()
			.and()
			//.oauth2Login()
			//.and()
			//.oauth2Client()
			//.and()
			.rememberMe()
			.userDetailsService(userDetailsService)
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
	HfhkUserService hfhkUserServices(MongoTemplate mongoTemplate) {
		return new HfhkUserService(mongoTemplate);
	}

}
