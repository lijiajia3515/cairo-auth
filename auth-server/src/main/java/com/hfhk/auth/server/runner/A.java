package com.hfhk.auth.server.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class A implements ApplicationRunner {
	private final MongoTemplate mongoTemplate;

	private final PasswordEncoder passwordEncoder;

	public A(MongoTemplate mongoTemplate, PasswordEncoder passwordEncoder) {
		this.mongoTemplate = mongoTemplate;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		ClientMongo auth = ClientMongo.builder()
//			.clientId("auth")
//			.clientSecret("{noop}123456")
//			.resourceIds(Collections.singletonList("auth"))
//			.grantTypes(Arrays.asList("authorization_code", "refresh_token", "password"))
//			.scopes(new ArrayList<>() {{
//				add(new ClientMongo.Scope("openid", true));
//				add(new ClientMongo.Scope("default", false));
//			}})
//			.authorities(Collections.singletonList("default"))
//			.redirectUris(Arrays.asList("http://127.0.0.1/#*", "http://127.0.0.1/*"))
//
//			.accessTokenValiditySeconds(7200)
//			.refreshTokenValiditySeconds(3600 * 24 * 7)
//			.build();
//
//		ClientMongo system = ClientMongo.builder()
//			.clientId("system")
//			.clientSecret("{noop}123456")
//			.resourceIds(Collections.emptyList())
//			.grantTypes(Collections.singletonList("client_credentials"))
//			.scopes(new ArrayList<>() {{
//				add(new ClientMongo.Scope("openid", true));
//				add(new ClientMongo.Scope("default", false));
//			}})
//			.authorities(Collections.singletonList("default"))
//			.redirectUris(Arrays.asList("http://127.0.0.1/#*", "http://127.0.0.1/*"))
//			.accessTokenValiditySeconds(7200)
//			.refreshTokenValiditySeconds(3600 * 24 * 7)
//			.build();
//
//		ClientMongo demo = ClientMongo.builder()
//			.clientId("demo")
//			.clientSecret("{noop}123456")
//			.resourceIds(Collections.emptyList())
//			.grantTypes(Collections.singletonList("client_credentials"))
//			.scopes(new ArrayList<>() {{
//				add(new ClientMongo.Scope("openid", true));
//				add(new ClientMongo.Scope("default", false));
//			}})
//			.authorities(Collections.singletonList("default"))
//			.redirectUris(Arrays.asList("http://127.0.0.1/#*", "http://127.0.0.1/*"))
//			.accessTokenValiditySeconds(7200)
//			.refreshTokenValiditySeconds(3600 * 24 * 7)
//			.build();
//
//		mongoTemplate.save(auth);
//		mongoTemplate.save(system);
//		mongoTemplate.save(demo);

//		UserMongo user1 = UserMongo.builder()
//			.id("0")
//			.username("admin")
//			.phoneNumber(null)
//			.email("admin@haofangsoft.com")
//			.password(passwordEncoder.encode("admin"))
//			.authorities(Collections.singletonList("DEFAULT"))
//			.roleCodes(Collections.singletonList("admin"))
//			.departmentCodes(Collections.singletonList("admin"))
//			.accountEnabled(true)
//			.accountLocked(false)
//			.build();
//		mongoTemplate.save(user1);
	}
}
