package com.hfhk.auth.service.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests(authorizeRequests -> authorizeRequests
				.mvcMatchers("/test/**").permitAll()
				.mvcMatchers("/**").authenticated()
			)
			.oauth2Login()
			.and()
			//.oauth2Login(oauth2Login ->
			//	oauth2Login.loginPage("/oauth2/authorization/hfhk-dhgxjsj"))
			//.oauth2Client(withDefaults())
			//.oauth2ResourceServer()
			//.jwt()
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


	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
