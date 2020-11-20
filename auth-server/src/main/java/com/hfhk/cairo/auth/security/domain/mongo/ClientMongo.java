package com.hfhk.cairo.auth.security.domain.mongo;

import com.hfhk.cairo.auth.security.constants.Mongo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;
import java.util.Map;

@Document(collection = Mongo.Collection.CLIENT)

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ClientMongo {
	@MongoId
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
