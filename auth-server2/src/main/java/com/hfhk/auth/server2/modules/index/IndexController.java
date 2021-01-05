package com.hfhk.auth.server2.modules.index;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
	@RequestMapping
	public Authentication index(Authentication authentication) {
		return authentication;
	}
}
