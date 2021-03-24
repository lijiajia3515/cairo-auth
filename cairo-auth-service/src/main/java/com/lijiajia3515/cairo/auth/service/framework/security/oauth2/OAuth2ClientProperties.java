package com.lijiajia3515.cairo.auth.service.framework.security.oauth2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.AccessType;

import java.util.List;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuth2ClientProperties {
	private String id;
	private String clientId;
	private String clientSecret;
	private List<String> scopes;
}
