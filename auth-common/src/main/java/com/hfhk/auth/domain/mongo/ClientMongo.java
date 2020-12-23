package com.hfhk.auth.domain.mongo;

import com.hfhk.cairo.domain.Metadata;
import com.hfhk.cairo.mongo.data.mapping.model.UpperCamelCaseFieldNames;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;

/**
 * Client
 */

@CompoundIndexes(
	@CompoundIndex(

	)
)
@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientMongo {
	private String id;


	private String clientId;

	private String clientSecret;

	@Builder.Default
	private Set<String> clientAuthenticationMethods = Collections.emptySet();

	@Builder.Default
	private Set<String> authorizationGrantTypes = Collections.emptySet();

	@Builder.Default
	private Set<String> scopes = Collections.emptySet();

	@Builder.Default
	private Set<String> redirectUris = Collections.emptySet();

	@Builder.Default
	private ClientSettings clientSettings = new ClientSettings();

	@Builder.Default
	private TokenSettings tokenSettings = new TokenSettings();

	@Builder.Default
	private Boolean enabled = false;

	@Builder.Default
	private Metadata metadata = new Metadata();

	@Data
	@Accessors(chain = true)
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ClientSettings {
		Boolean requireProofKey;
		Boolean requireUserConsent;
	}

	@Data
	@Accessors(chain = true)
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class TokenSettings {
		private Duration accessTokenTimeToLive;
		private Boolean reuseRefreshTokens;
		private Duration refreshTokenTimeToLive;
	}

	public static final class Field extends UpperCamelCaseFieldNames {
		public static final String ClientId = "ClientId";
		public static final String ClientSecret = "ClientSecret";
		public static final String ClientAuthenticationMethods = "ClientAuthenticationMethods";
		public static final String AuthorizationGrantTypes = "AuthorizationGrantTypes";
		public static final String Scopes = "Scopes";
		public static final String RedirectUris = "RedirectUris";
		public static final String ClientSettings = "ClientSettings";
		public static final String TokenSettings = "TokenSettings";
		public static final String Enabled = "Enabled";

		public static final class ClientSettings {
			public static final String RequireProofKey = "RequireProofKey";
			public static final String RequireUserConsent = "RequireUserConsent";
		}

		public static final class TokenSettings {
			public static final String AccessTokenTimeToLive = "AccessTokenTimeToLive";
			public static final String ReuseRefreshTokens = "ReuseRefreshTokens";
			public static final String RefreshTokenTimeToLive = "RefreshTokenTimeToLive";
		}
	}
}
