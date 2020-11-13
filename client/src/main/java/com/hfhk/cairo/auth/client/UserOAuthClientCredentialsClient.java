package com.hfhk.cairo.auth.client;

import com.hfhk.cairo.auth.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service_auth_v1", path = "/service/user", contextId = "serviceAuthV1UserOAuthClientCredentialsClient")
public interface UserOAuthClientCredentialsClient {

	@GetMapping(path = "/{uid}")
	User findById(@PathVariable(name = "uid") String uid);
}
