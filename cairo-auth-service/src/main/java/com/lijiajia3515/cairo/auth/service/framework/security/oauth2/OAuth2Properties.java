package com.lijiajia3515.cairo.auth.service.framework.security.oauth2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Data

@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder

@ConfigurationProperties(prefix = "lijiajia3515.auth")
@Configuration

public class OAuth2Properties {
	private String server;

	@NestedConfigurationProperty
	private OAuth2ClientProperties client;

}
