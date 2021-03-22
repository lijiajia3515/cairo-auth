package com.lijiajia3515.cairo.auth.server.framework.oauth2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder

@ConfigurationProperties(prefix = "lijiajia3515.oauth2")
@Configuration
public class OAuth2JwkProperties {
	private String issuer;
	private Map<String, Item> jwks;

	@Data

	@Accessors(chain = true)
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Item {
		private String publicKey;
		private String privateKey;
	}
}
