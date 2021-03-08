package com.lijiajia3515.cairo.auth.service.modules;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class Api {

	@GetMapping()
	@PreAuthorize("isAuthenticated()")
	public Authentication auth(Authentication authentication) {
		return authentication;
	}

}
