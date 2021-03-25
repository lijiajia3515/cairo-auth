package com.lijiajia3515.cairo.auth.server.modules.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordParam {
	private String username;
	private String password;
	private String clientId;
	private Set<String> scopes;
	private String redirectUri;
	private String state;
}
