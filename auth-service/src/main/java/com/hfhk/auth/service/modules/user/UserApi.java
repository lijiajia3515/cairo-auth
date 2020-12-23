package com.hfhk.auth.service.modules.user;

import com.hfhk.auth.domain.ResourceTreeNode;
import com.hfhk.auth.domain.User;
import com.hfhk.auth.domain.request.UserFindRequest;
import com.hfhk.auth.domain.request.UserModifyRequest;
import com.hfhk.auth.domain.request.UserRegRequest;
import com.hfhk.auth.domain.request.UserResetPasswordRequest;
import com.hfhk.auth.service.modules.resource.ResourceService;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.security.oauth2.server.resource.authentication.CairoAuthenticationToken;
import com.hfhk.cairo.starter.web.handler.BusinessResult;
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
	@BusinessResult
	@PermitAll
	public void reg(@RequestBody UserRegRequest request) {
		userService.reg(request);
	}

	@PatchMapping
	@BusinessResult
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	public Optional<User> modify(@AuthenticationPrincipal CairoAuthenticationToken token, @RequestBody UserModifyRequest request) {
		// token.getClient().getId();
		String client = token.getToken().getAudience().stream().findFirst().orElse(null);
		return userService.modify(client, request);
	}

	@PatchMapping("/password_reset")
	@BusinessResult
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	public String passwordReset(@AuthenticationPrincipal CairoAuthenticationToken token, @RequestBody UserResetPasswordRequest request) {
		return userService.passwordReset(request);
	}

	@GetMapping
	@BusinessResult
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	public Page<User> find(@AuthenticationPrincipal CairoAuthenticationToken token,
						   @PageableDefault Pageable pageable,
						   UserFindRequest request) {
		// token.getClient().getId();
		String client = token.getToken().getAudience().stream().findFirst().orElse(null);

		return userService.find(client, request, pageable);
	}

	@GetMapping("/current")
	@PreAuthorize("isAuthenticated()")
	@BusinessResult
	public com.hfhk.cairo.security.authentication.User currentUser(@AuthenticationPrincipal CairoAuthenticationToken token) {
		return token.getUser();
	}

	@GetMapping("/current/resource_tree")
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	@BusinessResult
	public List<ResourceTreeNode> userResources(@AuthenticationPrincipal CairoAuthenticationToken token) {
		// token.getClient().getId();
		String client = token.getToken().getAudience().stream().findFirst().orElse(null);
		return resourceService.treeFindByUid(client, token.getUser().getUid());
	}

}
