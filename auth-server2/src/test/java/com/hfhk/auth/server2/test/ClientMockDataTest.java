package com.hfhk.auth.server2.test;

import com.hfhk.auth.server2.AuthServer2App;
import com.hfhk.auth.server2.modules.client.ClientService;
import com.hfhk.cairo.core.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;

@SpringBootTest(classes = AuthServer2App.class)
@ExtendWith(SpringExtension.class)
class ClientMockDataTest {

	@Autowired
	private ClientService clientService;

	@Test
	public void test1() {
		//Junit5使用Assertions实现断言
		//arg1: 期望值 arg2: 结果值
		Assertions.assertEquals("你好世界", "你好世界");
	}

	@Test
	public void clientInit() {
		RegisteredClient testClient = RegisteredClient.withId(Constants.SNOWFLAKE.nextIdStr())
			.clientId("hfhk_test")
			.clientSecret("hfhk_test")
			.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
			.clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
			.clientAuthenticationMethod(ClientAuthenticationMethod.POST)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.authorizationGrantType(AuthorizationGrantType.PASSWORD)
			.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
			.authorizationGrantType(AuthorizationGrantType.IMPLICIT)
			.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
			.scope("openid")
			.scope("userinfo")
			.scope("message.read")
			.scope("message.write")
			.redirectUri("http://localhost:10000")
			.redirectUri("http://localhost:10000/login/oauth2/code/hfhk_test")
			.clientSettings(setting -> setting.requireUserConsent(true).requireProofKey(true))
			.tokenSettings(setting -> setting
				.accessTokenTimeToLive(Duration.ofDays(60))
				.refreshTokenTimeToLive(Duration.ofDays(365)))
			.build();

		RegisteredClient dhgxjsjClient = RegisteredClient.withId(Constants.SNOWFLAKE.nextIdStr())
			.clientId("hfhk_dhgxjsj")
			.clientSecret("hfhk_dhgxjsj")
			.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
			.clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
			.clientAuthenticationMethod(ClientAuthenticationMethod.POST)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.authorizationGrantType(AuthorizationGrantType.PASSWORD)
			.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
			.authorizationGrantType(AuthorizationGrantType.IMPLICIT)
			.scope("openid")
			.scope("userinfo")
			.scope("message.read")
			.scope("message.write")
			.redirectUri("http://localhost:10000")
			.redirectUri("http://localhost:10000/login/oauth2/code/hfhk_dhgxjsj")
			.clientSettings(setting -> setting.requireUserConsent(true).requireProofKey(true))
			.tokenSettings(setting -> setting
				.accessTokenTimeToLive(Duration.ofDays(60))
				.refreshTokenTimeToLive(Duration.ofDays(365)))
			.build();
		clientService.save(testClient);
		clientService.save(dhgxjsjClient);
	}


}
