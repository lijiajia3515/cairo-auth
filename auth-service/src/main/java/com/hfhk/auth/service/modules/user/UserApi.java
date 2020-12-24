package com.hfhk.auth.service.modules.user;

import com.hfhk.auth.domain.resource.ResourceTreeNode;
import com.hfhk.auth.domain.user.*;
import com.hfhk.auth.service.modules.resource.ResourceService;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.security.oauth2.user.AuthPrincipal;
import com.hfhk.cairo.starter.service.web.handler.BusinessResult;
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

	private final ResourceService resourceService;

	public UserApi(UserService userService, ResourceService resourceService) {
		this.userService = userService;
		this.resourceService = resourceService;
	}

	@PostMapping("/Reg")
	@PermitAll
	@BusinessResult
	public void reg(@RequestBody UserRegRequest request) {
		userService.reg(request);
	}

	@PatchMapping
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	@BusinessResult
	public User modify(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody UserModifyRequest request) {
		// auth.getClient().getId();
		String client = principal.getClient();
		return userService.modify(client, request).orElseThrow();
	}

	@PatchMapping("/PasswordReset")
	@PreAuthorize("isAuthenticated()")
	@BusinessResult
	public String passwordReset(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody UserResetPasswordRequest request) {
		String client = principal.getClient();
		return userService.passwordReset(request);
	}

	@PostMapping("/Find")
	@BusinessResult
	@PreAuthorize("isAuthenticated()")
	public Page<User> find(@AuthenticationPrincipal AuthPrincipal principal,
						   @RequestBody UserPageFindRequest request) {
		String client = principal.getClient();
		return userService.find(client, request);
	}

	@PostMapping("/Current")
	@PreAuthorize("isAuthenticated()")
	@BusinessResult
	public com.hfhk.cairo.domain.auth.User currentUser(@AuthenticationPrincipal AuthPrincipal principal) {

		return principal.getUser();
	}

	@GetMapping("/Current/ResourceTree")
	@PreAuthorize("isAuthenticated()")
	@BusinessResult
	public List<ResourceTreeNode> userResources(@AuthenticationPrincipal AuthPrincipal principal) {
		String client = principal.getClient();

		return resourceService.treeFindByUid(client, principal.getUser().getUid());
	}

}
