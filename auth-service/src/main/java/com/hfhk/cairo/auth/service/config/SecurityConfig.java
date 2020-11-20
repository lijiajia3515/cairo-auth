package com.hfhk.cairo.auth.service.config;


import com.hfhk.cairo.security.oauth2.expression.CairoMethodSecurityExpressionHandler;
import com.hfhk.cairo.security.oauth2.expression.CairoWebSecurityExpressionHandler;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.FilterInvocation;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

	@Configuration
	@EnableWebSecurity
	public static class CairoWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
		private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;
		private final Converter<Jwt, ? extends AbstractAuthenticationToken> authenticationConverter;

		public CairoWebSecurityConfiguration(OAuth2ResourceServerProperties oAuth2ResourceServerProperties, Converter<Jwt, ? extends AbstractAuthenticationToken> authenticationConverter) {
			this.oAuth2ResourceServerProperties = oAuth2ResourceServerProperties;
			this.authenticationConverter = authenticationConverter;
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			super.configure(auth);
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			super.configure(web);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();

			http.oauth2ResourceServer()
				.jwt()
				.jwkSetUri(oAuth2ResourceServerProperties.getJwt().getJwkSetUri())
				.jwtAuthenticationConverter(authenticationConverter)
			;


			// 异常处理
			http.exceptionHandling();

			http.authorizeRequests()
				.anyRequest().authenticated();

		}
	}

	@Bean
	public SecurityExpressionHandler<FilterInvocation> cairoWebSecurityExpressionHandler() {
		return new CairoWebSecurityExpressionHandler();
	}

	@Bean
	public MethodSecurityExpressionHandler cairoMethodSecurityExpressionHandler() {
		return new CairoMethodSecurityExpressionHandler();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
