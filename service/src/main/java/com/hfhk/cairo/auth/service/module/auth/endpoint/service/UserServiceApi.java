package com.hfhk.cairo.auth.service.module.auth.endpoint.service;

import com.hfhk.cairo.auth.domain.User;
import com.hfhk.cairo.security.oauth2.server.resource.authentication.CairoAuthenticationToken;
import com.hfhk.cairo.auth.service.module.auth.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service/user")
public class UserServiceApi {
	private final UserService userService;

	public UserServiceApi(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{uid}")
	public User findById(@AuthenticationPrincipal CairoAuthenticationToken token, @PathVariable String uid) {
		return userService.findById(token.getClient().getId(), uid);
	}
}
