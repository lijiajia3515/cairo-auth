//package com.hfhk.harpoon.auth;
//
////import com.hfhk.cairo.auth.security.domain.mongo.ClientMongo;
////import com.hfhk.cairo.auth.security.domain.mongo.UserMongo;
////import org.junit.jupiter.api.Test;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.boot.test.context.SpringBootTest;
////import org.springframework.data.mongodb.core.MongoTemplate;
////import org.springframework.security.crypto.password.PasswordEncoder;
////
////import java.util.ArrayList;
////import java.util.Arrays;
////import java.util.Collections;
//
////@SpringBootTest
//class AuthApplicationTests {
//	//@Autowired
//	//private MongoTemplate mongoTemplate;
//	//
//	//@Autowired
//	//private PasswordEncoder passwordEncoder;
//
//	//@Test
//	void userInit() {
//		//UserMongo user1 = UserMongo.builder()
//		//	.id("0")
//		//	.username("admin")
//		//	.phoneNumber("12345678901")
//		//	.email("admin@haofangsoft.com")
//		//	.password(passwordEncoder.encode("admin"))
//		//	// .authorities(Collections.singletonList("DEFAULT"))
//		//	.roleCodes(Collections.singletonList("admin"))
//		//	.departmentCodes(Collections.singletonList("admin"))
//		//	.accountEnabled(true)
//		//	.accountLocked(false)
//		//	.build();
//		//mongoTemplate.save(user1);
//	}
//
//	//@Test
//	void clientInit() {
//		//ClientMongo auth = ClientMongo.builder()
//		//	.clientId("auth")
//		//	.clientSecret("{noop}123456")
//		//	.resourceIds(Collections.singletonList("auth"))
//		//	.grantTypes(Arrays.asList("authorization_code", "refresh_token", "password"))
//		//	.scopes(new ArrayList<>() {{
//		//		add(new ClientMongo.Scope("openid", true));
//		//		add(new ClientMongo.Scope("default", false));
//		//	}})
//		//	.authorities(Arrays.asList("default", "auth2"))
//		//	.redirectUris(Arrays.asList("http://127.0.0.1/#*", "http://127.0.0.1/*"))
//		//
//		//	.accessTokenValiditySeconds(7200)
//		//	.refreshTokenValiditySeconds(3600 * 24 * 7)
//		//	.build();
//		//
//		//ClientMongo system = ClientMongo.builder()
//		//	.clientId("system")
//		//	.clientSecret("{noop}123456")
//		//	.resourceIds(Collections.emptyList())
//		//	.grantTypes(Collections.singletonList("client_credentials"))
//		//	.authorities(Arrays.asList("default", "auth2"))
//		//	.redirectUris(Arrays.asList("http://127.0.0.1/#*", "http://127.0.0.1/*"))
//		//	.accessTokenValiditySeconds(7200)
//		//	.refreshTokenValiditySeconds(3600 * 24 * 7)
//		//	.build();
//		//
//		//ClientMongo demo = ClientMongo.builder()
//		//	.clientId("demo")
//		//	.clientSecret("{noop}123456")
//		//	.resourceIds(Collections.emptyList())
//		//	.grantTypes(Collections.singletonList("client_credentials"))
//		//	.authorities(Arrays.asList("default", "auth2"))
//		//	.redirectUris(Arrays.asList("http://127.0.0.1/#*", "http://127.0.0.1/*"))
//		//	.accessTokenValiditySeconds(7200)
//		//	.refreshTokenValiditySeconds(3600 * 24 * 7)
//		//	.build();
//		//mongoTemplate.save(auth);
//		//mongoTemplate.save(system);
//		//mongoTemplate.save(demo);
//	}
//
//}
