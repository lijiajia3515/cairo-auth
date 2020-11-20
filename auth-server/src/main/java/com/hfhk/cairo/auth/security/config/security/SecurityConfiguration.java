package com.hfhk.cairo.auth.security.config.security;

import com.hfhk.cairo.auth.security.userdetails.AuthPasswordService;
import com.hfhk.cairo.auth.security.userdetails.AuthUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class SecurityConfiguration {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public UserDetailsService myUserDetailsService(MongoTemplate mongoTemplate) {
		return new AuthUserService(mongoTemplate);
	}

	@Bean
	public UserDetailsPasswordService myPasswordService(MongoTemplate mongoTemplate) {
		return new AuthPasswordService(mongoTemplate);
	}

	@EnableWebSecurity
	@Configuration
	public static class SecurityConfig extends WebSecurityConfigurerAdapter {
		private final UserDetailsService userDetailsService;
		private final UserDetailsPasswordService userDetailsPasswordService;

		public SecurityConfig(
			@Qualifier("myUserDetailsService") UserDetailsService userDetailsService,
			@Qualifier("myPasswordService") UserDetailsPasswordService userDetailsPasswordService
		) {
			this.userDetailsService = userDetailsService;
			this.userDetailsPasswordService = userDetailsPasswordService;
		}

		@Override
		@Bean("harpoonAuthenticationManagerBean")
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService)
				.userDetailsPasswordManager(userDetailsPasswordService);

			//auth.inMemoryAuthentication()
			//	.withUser("admin")
			//	.password("{noop}123456")
			//	.authorities("DEFAULT")
			//	.roles("ADMIN")
			//	.and()
			//	.withUser("guest")
			//	.password("{noop}123456")
			//	.authorities("DEFAULT")
			//	.roles("GUEST");
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			super.configure(web);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.csrf().disable()
				.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/login")
				//.successHandler()
				//.failureHandler()
				.and()
				.rememberMe()
				.and()
				.authorizeRequests()
				.mvcMatchers("/principal").authenticated()
				.mvcMatchers("/.well-known/jwks.json").permitAll()
			;
		}
	}
}
