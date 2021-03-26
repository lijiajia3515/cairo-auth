package com.lijiajia3515.cairo.auth.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ClientV2Mongo {
	private String id;

	private String clientId;
	private String clientSecret;

	private List<String> grantTypes;

	private List<String> resourceIds;

	private List<Scope> scopes;
	private List<String> authorities;

	private List<String> redirectUris;

	private Integer accessTokenValiditySeconds;
	private Integer refreshTokenValiditySeconds;

	private Map<String, Object> additionalInformation;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Scope {
		private String name;
		private boolean autoApprove;
	}

}
