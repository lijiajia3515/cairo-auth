package com.lijiajia3515.auth.domain.mongo;

import com.lijiajia3515.cairo.domain.Metadata;
import com.lijiajia3515.cairo.mongo.data.mapping.model.AbstractUpperCamelCaseField;
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

	public static final Field FIELD = new Field();

	public static final class Field extends AbstractUpperCamelCaseField {
		private Field() {

		}

		public final String CLIENT_ID = field("ClientId");
		public final String CLIENT_SECRET = field("ClientSecret");
		public final String CLIENT_AUTHENTICATION_METHODS = field("ClientAuthenticationMethods");
		public final String AUTHORIZATION_GRANT_TYPES = field("AuthorizationGrantTypes");
		public final String SCOPES = field("Scopes");
		public final String REDIRECT_URIS = field("RedirectUris");
		public final String CLIENT_SETTINGS = field("ClientSettings");
		public final String TOKEN_SETTINGS = field("TokenSettings");
		public final String ENABLED = field("Enabled");

		public static final class ClientSettings extends AbstractUpperCamelCaseField {
			public final String REQUIRE_PROOF_KEY = field("RequireProofKey");
			public final String REQUIRE_USER_CONSENT = field("RequireUserConsent");
		}

		public static final class TokenSettings extends AbstractUpperCamelCaseField {
			public final String ACCESS_TOKEN_TIME_TO_LIVE = field("AccessTokenTimeToLive");
			public final String REUSE_REFRESH_TOKENS = field("ReuseRefreshTokens");
			public final String REFRESH_TOKEN_TIME_TO_LIVE = field("RefreshTokenTimeToLive");
		}
	}
}
