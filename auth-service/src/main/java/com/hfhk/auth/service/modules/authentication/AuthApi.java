package com.hfhk.auth.service.modules.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j

@RestController
@RequestMapping("/authentication")
public class AuthApi {

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public Authentication authenticationToken(Authentication authentication) {
		return authentication;
	}
}
