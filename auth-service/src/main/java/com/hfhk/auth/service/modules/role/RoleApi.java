package com.hfhk.auth.service.modules.role;

import com.hfhk.auth.domain.role.*;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.security.oauth2.user.AuthPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Role Api
 */
@RestController
@RequestMapping("/Role")
public class RoleApi {
	private final RoleService roleService;

	public RoleApi(RoleService roleService) {
		this.roleService = roleService;
	}

	// Operate

	@PostMapping("/Save")
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	public void save(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody RoleSaveParam request) {
		String client = principal.getClient();
		roleService.save(client, request);
	}

	@PutMapping("/Modify")
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	public void modify(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody RoleModifyParam request) {
		String client = principal.getClient();

		roleService.modify(client, request);
	}

	@DeleteMapping("/Delete")
	@PreAuthorize("isAuthenticated()")
	public void delete(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody RoleDeleteParam param) {
		String client = principal.getClient();

		roleService.delete(client, param);
	}

	// Find

	@PostMapping("/Find")
	@PreAuthorize("isAuthenticated()")
	public Page<Role> pageFind(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody RolePageFindParam request) {
		String client = principal.getClient();

		return roleService.pageFind(client, request);
	}
}
