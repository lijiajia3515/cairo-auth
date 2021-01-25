package com.hfhk.auth.modules.auth;

import com.hfhk.cairo.security.authentication.RemoteUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "service-auth-v1", path = "/auth", contextId = "service-auth-v1-auth-basic-client")
public interface AuthBasicClient {

	@GetMapping
	RemoteUser auth(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authentication);
}