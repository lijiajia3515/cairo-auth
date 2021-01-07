package com.hfhk.auth.service.modules.user;

import com.hfhk.auth.domain.user.*;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.security.oauth2.user.AuthPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Optional;

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
	public User reg(@RequestBody UserSaveParam request) {
		return userService.reg(request);
	}

	@PostMapping("/Save")
	@PreAuthorize("isAuthenticated()")
	public User save(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody UserSaveParam param) {
		String client = principal.getClient();
		return userService.save(client, param);
	}

	@PutMapping("/Modify")
	@PreAuthorize("isAuthenticated()")
	public User modify(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody UserModifyParam request) {
		String client = principal.getClient();
		return userService.modify(client, request).orElseThrow();
	}

	@PatchMapping("/PasswordReset")
	@PreAuthorize("isAuthenticated()")
	public Optional<String> passwordReset(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody UserResetPasswordParam param) {
		String client = principal.getClient();
		return userService.resetPassword(client, param);
	}

	@PatchMapping("/StatusModify")
	@PreAuthorize("isAuthenticated()")
	public Optional<Boolean> modifyStatus(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody UserModifyStatusParam param) {
		String client = principal.getClient();
		return userService.modifyStatus(client, param);
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

	@GetMapping("/Find/{uid}")
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
