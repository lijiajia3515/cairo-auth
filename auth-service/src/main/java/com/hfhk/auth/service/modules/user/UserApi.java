package com.hfhk.auth.service.modules.user;

import com.hfhk.auth.domain.user.*;
import com.hfhk.auth.service.modules.resource.ResourceService;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.security.oauth2.user.AuthPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

/**
 * User
 */
@RestController
@RequestMapping("/User")
public class UserApi {

	private final UserService userService;

	public UserApi(UserService userService) {
		this.userService = userService;
	}

	// operate
	@PostMapping("/Reg")
	@PermitAll
	public void reg(@RequestBody UserRegParam request) {
		userService.reg(request);
	}

	@PatchMapping("/Modify")
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	public User modify(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody UserModifyParam request) {
		// auth.getClient().getId();
		String client = principal.getClient();
		return userService.modify(client, request).orElseThrow();
	}

	@PatchMapping("/PasswordReset")
	@PreAuthorize("isAuthenticated()")
	public String passwordReset(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody UserResetPasswordParam request) {
		String client = principal.getClient();
		return userService.passwordReset(request);
	}

	// find
	@PostMapping("Find")
	@PreAuthorize("isAuthenticated()")
	public List<User> find(@AuthenticationPrincipal AuthPrincipal principal,
						   @RequestBody(required = false) UserFindParam param) {
		String client = principal.getClient();
		return userService.find(client, param);
	}

	@PostMapping("/FindPage")
	@PreAuthorize("isAuthenticated()")
	public Page<User> findPage(@AuthenticationPrincipal AuthPrincipal principal,
						   @RequestBody UserFindParam param) {
		String client = principal.getClient();
		return userService.findPage(client, param);
	}

	@PostMapping("/FindById/{uid}")
	@PreAuthorize("isAuthenticated()")
	public User findById(@AuthenticationPrincipal AuthPrincipal principal, @PathVariable String uid) {
		String client = principal.getClient();
		return userService.findById(client, uid).orElseThrow();
	}

	// current
	@GetMapping("/Current")
	@PreAuthorize("isAuthenticated()")
	public com.hfhk.cairo.domain.auth.User currentUser(@AuthenticationPrincipal AuthPrincipal principal) {
		return principal.getUser();
	}

}
