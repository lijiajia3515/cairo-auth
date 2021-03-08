package com.lijiajia3515.cairo.auth.server.test;

import com.lijiajia3515.auth.domain.mongo.Mongo;
import com.lijiajia3515.auth.domain.mongo.UserMongo;
import com.lijiajia3515.cairo.auth.server.AuthServerConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

@SpringBootTest
class UserMockDataTest {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void userInit() {
		UserMongo root = UserMongo.builder()
			.id("root")
			.uid("root")
			.username("root")
			.password(passwordEncoder.encode("root"))
			.email("root@haofangsoft.com")
			.phoneNumber("root")
			.clientRoles(new HashMap<>() {{
				put(AuthServerConstant.CLIENT, new HashSet<>() {{
					add("ADMIN");
				}});
			}})
			.clientDepartments(new HashMap<>() {{
				put(AuthServerConstant.CLIENT, Collections.emptySet());
			}})
			.clientResources(new HashMap<>() {{
				put(AuthServerConstant.CLIENT, Collections.emptySet());
			}})
			.clientAuthorities(new HashMap<>() {{
				put(AuthServerConstant.CLIENT, Collections.emptySet());
			}})
			//.roleCodes(Collections.singletonList("admin"))
			//.departmentCodes(Collections.singletonList("admin"))
			.accountEnabled(true)
			.accountLocked(false)
			.build();
		mongoTemplate.save(root, Mongo.Collection.USER);
	}

}
