package com.hfhk.cairo.auth.client;

import com.hfhk.cairo.security.authentication.RemoteUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "service-auth-v1", path = "/service/authentication", contextId = "serviceAuthV1AuthenticationBasicClient")
public interface AuthenticationBasicClient {

	@GetMapping
    RemoteUser authentication(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authentication);
}
