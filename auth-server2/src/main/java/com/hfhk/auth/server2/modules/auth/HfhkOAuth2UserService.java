package com.hfhk.auth.server2.modules.auth;

import com.hfhk.auth.domain.mongo.Mongo;
import com.hfhk.auth.domain.mongo.UserMongo;
import com.hfhk.auth.server2.modules.auth.oauth2.client.userinfo.CommonOAuth2UserInfoResponseClient;
import com.hfhk.auth.server2.modules.auth.oauth2.client.userinfo.OAuth2UserInfoResponseClient;
import com.hfhk.auth.server2.modules.auth.oauth2.client.userinfo.CommonOAuth2UserRequestEntityConverter;
import com.hfhk.cairo.core.Constants;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

public class HfhkOAuth2UserService extends AbstractPrincipalService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private static final String MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri";

	private static final String MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE = "missing_user_name_attribute";

	private Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new CommonOAuth2UserRequestEntityConverter();
	private OAuth2UserInfoResponseClient userInfoResponseClient = new CommonOAuth2UserInfoResponseClient();


	public HfhkOAuth2UserService(MongoTemplate mongoTemplate) {
		super(mongoTemplate);
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		Assert.notNull(userRequest, "userRequest cannot be null");
		if (!StringUtils.hasText(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri())) {
			OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_INFO_URI_ERROR_CODE,
				"Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: "
					+ userRequest.getClientRegistration().getRegistrationId(),
				null);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
		}
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		if (!StringUtils.hasText(userNameAttributeName)) {
			OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE, "Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: " + userRequest.getClientRegistration().getRegistrationId(), null);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
		}
		RequestEntity<?> request = this.requestEntityConverter.convert(userRequest);
		ResponseEntity<Map<String, Object>> response = userInfoResponseClient.getResponse(userRequest, request);
		Map<String, Object> userAttributes = Optional.ofNullable(response.getBody()).orElse(Collections.emptyMap());
		Set<GrantedAuthority> authorities = new LinkedHashSet<>();
		authorities.add(new OAuth2UserAuthority(userAttributes));
		OAuth2AccessToken token = userRequest.getAccessToken();
		for (String authority : token.getScopes()) {
			authorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));
		}
		String connection = userRequest.getClientRegistration().getRegistrationId();
		String subject = userAttributes.get(userNameAttributeName).toString();
		Query query = Query.query(Criteria
			.where(UserMongo.FIELD.CONNECTIONS.CONNECTION).is(connection)
			.and(UserMongo.FIELD.CONNECTIONS.SUBJECT).is(subject)
			.and(UserMongo.FIELD.CONNECTIONS.ENABLED).is(true)
		).with(Sort.by(
			Sort.Order.asc(UserMongo.FIELD.METADATA.CREATED.AT),
			Sort.Order.asc(UserMongo.FIELD.METADATA.LAST_MODIFIED.AT),
			Sort.Order.asc(UserMongo.FIELD._ID)
		));
		return principal(query)
			.or(() -> Optional.of(createOAuthUser(connection, subject)))
			.map(x -> {
				x.setAttributes(userAttributes);
				x.getAuthorities().addAll(authorities);
				return x;
			}).orElseThrow();
	}


	/**
	 * Sets the {@link Converter} used for converting the {@link OAuth2UserRequest} to a
	 * {@link RequestEntity} representation of the UserInfo Request.
	 *
	 * @param requestEntityConverter the {@link Converter} used for converting to a
	 *                               {@link RequestEntity} representation of the UserInfo Request
	 * @since 5.1
	 */
	public final void setRequestEntityConverter(Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter) {
		Assert.notNull(requestEntityConverter, "requestEntityConverter cannot be null");
		this.requestEntityConverter = requestEntityConverter;
	}

	@Transactional(rollbackFor = Exception.class)
	public AuthUser createOAuthUser(String connection, String subject) {
		String uid = Constants.SNOWFLAKE.nextIdStr();
		UserMongo mongo = UserMongo.builder()
			.uid(uid)
			.username(uid)
			.clientRoles(Collections.emptyMap())
			.clientDepartments(Collections.emptyMap())
			.clientAuthorities(Collections.emptyMap())
			.clientResources(Collections.emptyMap())
			.connections(Collections.singletonList(UserMongo.Connection.builder().connection(connection).subject(subject).enabled(true).createdAt(LocalDateTime.now()).build()))
			.build();

		mongo = mongoTemplate.insert(mongo, Mongo.Collection.USER);
		return principal(mongo);
	}
}
