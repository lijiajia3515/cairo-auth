package com.hfhk.auth.service.modules.user;

import com.hfhk.auth.domain.user.User;
import com.hfhk.cairo.security.oauth2.server.resource.authentication.CairoAuthentication;
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
	public User findById(@AuthenticationPrincipal CairoAuthentication auth, @PathVariable String uid) {
		// token.getClient().getId();
		String client = auth.getToken().getAudience().stream().findFirst().orElse(null);
		return userService.findById(client, uid);
	}
}
