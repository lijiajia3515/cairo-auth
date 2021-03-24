package com.lijiajia3515.cairo.auth.service.modules;

import com.lijiajia3515.cairo.auth.modules.user.User;
import com.lijiajia3515.cairo.auth.modules.user.UserFindParam;
import com.lijiajia3515.cairo.auth.service.AuthServiceApp;
import com.lijiajia3515.cairo.auth.service.client.UserClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = AuthServiceApp.class)
public class UserV1Tests {

	@Autowired
	UserClient userClient;

	@Test
	public void test() {
		List<User> users = userClient.find(UserFindParam.builder().build());
		System.out.println(users);


	}

}
