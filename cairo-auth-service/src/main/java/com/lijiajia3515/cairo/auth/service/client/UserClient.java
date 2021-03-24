package com.lijiajia3515.cairo.auth.service.client;

import com.lijiajia3515.cairo.auth.modules.user.User;
import com.lijiajia3515.cairo.auth.modules.user.UserFindParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(contextId = "userV1Client", name = "service-auth-v2", path = "/User")
public interface UserClient {

	@PostMapping("/Find")
	List<User> find(@RequestBody UserFindParam param);
}
