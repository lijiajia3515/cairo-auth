package com.lijiajia3515.cairo.auth.service.client;

import com.lijiajia3515.cairo.security.authentication.RemoteUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(contextId = "auth2Client", name = "${lijiajia3515.auth.server}", url = "${lijiajia3515.auth.server}", path = "/")
public interface Auth2Client {

	@GetMapping("/authentication")
	RemoteUser authentication(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authentication);
}
