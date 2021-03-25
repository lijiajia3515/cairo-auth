package com.lijiajia3515.cairo.auth.server.framework.security.oauth2.client.userinfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum OAuth2ProviderConnection {
	Wechat("wechat", "wechat-web", "wechat-open", "wechat-mini-app"),
	Github("github"),
	Ali("alipay");
	public final Set<String> REGISTRATIONS;

	OAuth2ProviderConnection() {
		this.REGISTRATIONS = Collections.emptySet();
	}

	OAuth2ProviderConnection(String... registrations) {
		this.REGISTRATIONS = Stream.of(registrations).collect(Collectors.toSet());
	}

	public static String connection(String registration) {
		return Arrays.stream(OAuth2ProviderConnection.values())
			.filter(x -> x.REGISTRATIONS.contains(registration)).findFirst().map(Enum::name).orElse("Default");
	}
}
