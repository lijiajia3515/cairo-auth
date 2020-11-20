package com.hfhk.cairo.auth.security.oauth2.provider.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.*;

public class CairoUserAuthenticationConverter implements UserAuthenticationConverter {
	public static final String UID = "uid";

	public static final String AUTHORITIES = "authorities";

	private Collection<? extends GrantedAuthority> defaultAuthorities;

	/**
	 * Default value for authorities if an Authentication is being created and the input has no data for authorities.
	 * Note that unless this property is set, the default Authentication created by {@link #extractAuthentication(Map)}
	 * will be unauthenticated.
	 *
	 * @param defaultAuthorities the defaultAuthorities to set. Default null.
	 */
	public void setDefaultAuthorities(String... defaultAuthorities) {
		this.defaultAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.arrayToCommaDelimitedString(defaultAuthorities));
	}

	public Map<String, ?> convertUserAuthentication(Authentication authentication) {

		Collection<String> authorities = Optional.ofNullable(authentication.getAuthorities())
			.filter(x -> !x.isEmpty())
			.map(x -> AuthorityUtils.authorityListToSet(authentication.getAuthorities()))
			.orElse(Collections.emptySet());
		return new LinkedHashMap<>() {{
			put(UID, authentication.getName());
		}};
	}

	public Authentication extractAuthentication(Map<String, ?> map) {
		if (map.containsKey(UID)) {
			String uid = map.get(UID).toString();
			Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
			return new UsernamePasswordAuthenticationToken(uid, "N/A", authorities);
		}
		return null;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
		if (!map.containsKey(AUTHORITIES)) {
			return defaultAuthorities;
		}
		Object authorities = map.get(AUTHORITIES);
		if (authorities instanceof String) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
		}
		if (authorities instanceof Collection) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
				.collectionToCommaDelimitedString((Collection<?>) authorities));
		}
		throw new IllegalArgumentException("Authorities must be either a String or a Collection");
	}
}
