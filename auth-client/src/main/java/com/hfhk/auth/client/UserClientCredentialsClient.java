package com.hfhk.auth.client;

import com.hfhk.auth.domain.user.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-auth-v1", path = "/service/user", contextId = "serviceAuthV1UserOAuthClientCredentialsClient")
public interface UserClientCredentialsClient {

	@GetMapping(path = "/{uid}")
	User findById(@PathVariable(name = "uid") String uid);
}
