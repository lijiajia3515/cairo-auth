package com.hfhk.cairo.auth.service.module.auth.endpoint.api;

import com.hfhk.cairo.auth.domain.ResourceTreeNode;
import com.hfhk.cairo.auth.domain.User;
import com.hfhk.cairo.auth.domain.request.UserFindRequest;
import com.hfhk.cairo.auth.domain.request.UserModifyRequest;
import com.hfhk.cairo.auth.domain.request.UserRegRequest;
import com.hfhk.cairo.auth.domain.request.UserResetPasswordRequest;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.starter.web.handler.StatusResult;
import com.hfhk.cairo.security.oauth2.server.resource.authentication.CairoAuthenticationToken;
import com.hfhk.cairo.auth.service.module.auth.service.ResourceService;
import com.hfhk.cairo.auth.service.module.auth.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Optional;

/**
 * restful - user
 */
@RestController
@RequestMapping("/user")
public class UserApi {

	private final UserService userService;

	private final ResourceService resourceService;

	public UserApi(UserService userService, ResourceService resourceService) {
		this.userService = userService;
		this.resourceService = resourceService;
	}

	@PostMapping("/reg")
	@StatusResult
	@PermitAll
	public void reg(@RequestBody UserRegRequest request) {
		userService.reg(request);
	}

	@PatchMapping
	@StatusResult
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	public Optional<User> modify(@AuthenticationPrincipal CairoAuthenticationToken token, @RequestBody UserModifyRequest request) {
		return userService.modify(token.getClient().getId(), request);
	}

	@PatchMapping("/password_reset")
	@StatusResult
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	public String passwordReset(@AuthenticationPrincipal CairoAuthenticationToken token, @RequestBody UserResetPasswordRequest request) {
		return userService.passwordReset(request);
	}

	@GetMapping
	@StatusResult
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	public Page<User> find(@AuthenticationPrincipal CairoAuthenticationToken token,
						   @PageableDefault Pageable pageable,
						   UserFindRequest request) {
		String clientId = token.getClient().getId();

		return userService.find(clientId, request, pageable);
	}

	@GetMapping("/current")
	@PreAuthorize("isAuthenticated()")
	@StatusResult
	public com.hfhk.cairo.security.authentication.User currentUser(@AuthenticationPrincipal CairoAuthenticationToken token) {
		return token.getUser();
	}

	@GetMapping("/current/resource_tree")
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	@StatusResult
	public List<ResourceTreeNode> userResources(@AuthenticationPrincipal CairoAuthenticationToken token) {
		return resourceService.treeFindByUid(token.getClient().getId(), token.getUser().getUid());
	}

}
