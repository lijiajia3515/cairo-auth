package com.hfhk.auth.service.modules;

import com.hfhk.cairo.starter.service.web.handler.BusinessResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class Api {

	@GetMapping()
	@BusinessResult
	@PreAuthorize("isAuthenticated()")
	public Authentication auth(Authentication authentication) {
		return authentication;
	}

}
