package com.hfhk.cairo.auth.security.config.authorizationserver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "harpoon.oauth2.authorization.jwt")
@Configuration
public class JwtProperties {

	/**
	 * The location of the key store.
	 */
	private String keyStore;

	/**
	 * The key store's password
	 */
	private String keyStorePassword;

	/**
	 * The alias of the key from the key store
	 */
	private String keyAlias;

	/**
	 * The password of the key from the key store
	 */
	private String keyPassword;
}
