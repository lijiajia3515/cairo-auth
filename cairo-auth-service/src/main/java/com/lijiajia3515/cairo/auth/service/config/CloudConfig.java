package com.lijiajia3515.cairo.auth.service.config;

import com.lijiajia3515.cairo.auth.service.framework.security.oauth2.OAuth2Properties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.security.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

@Configuration
@EnableDiscoveryClient
@EnableFeignClients(
	basePackages = "com.lijiajia3515.cairo.**.client"
)
public class CloudConfig {

	@Bean
	public OAuth2ClientContext oauth2ClientContext() {
		return new DefaultOAuth2ClientContext();
	}

	@Bean
	public OAuth2ProtectedResourceDetails oauth2ResourceDetails(OAuth2Properties properties) {
		ClientCredentialsResourceDetails client = new ClientCredentialsResourceDetails();
		client.setId(properties.getClient().getId());
		client.setClientId(properties.getClient().getClientId());
		client.setClientSecret(properties.getClient().getClientSecret());
		client.setAccessTokenUri(String.format("http://%s/oauth2/token", properties.getServer()));
		client.setScope(properties.getClient().getScopes());
		return client;
	}

	@Bean
	public OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor(OAuth2ClientContext context, OAuth2ProtectedResourceDetails resource) {
		return new OAuth2FeignRequestInterceptor(context, resource);
	}
}
