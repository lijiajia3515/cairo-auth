package com.hfhk.auth.server2.test;

import com.hfhk.auth.domain.mongo.Mongo;
import com.hfhk.auth.domain.mongo.UserMongo;
import com.hfhk.auth.server2.Constant;
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
				put(Constant.Client, new HashSet<>() {{
					add("ADMIN");
				}});
			}})
			.clientDepartments(new HashMap<>() {{
				put(Constant.Client, Collections.emptySet());
			}})
			.clientResources(new HashMap<>() {{
				put(Constant.Client, Collections.emptySet());
			}})
			.clientAuthorities(new HashMap<>() {{
				put(Constant.Client, Collections.emptySet());
			}})
			//.roleCodes(Collections.singletonList("admin"))
			//.departmentCodes(Collections.singletonList("admin"))
			.accountEnabled(true)
			.accountLocked(false)
			.build();
		mongoTemplate.save(root, Mongo.Collection.User);
	}

}
