package com.lijiajia3515.cairo.auth.modules.auth.client;

import com.lijiajia3515.cairo.security.authentication.RemoteUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${lijiajia3515.auth.server}", path = "/", contextId = "authenticationClient")
public interface AuthenticationClient {

	@GetMapping("/authentication")
	RemoteUser authentication(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authentication);
}
