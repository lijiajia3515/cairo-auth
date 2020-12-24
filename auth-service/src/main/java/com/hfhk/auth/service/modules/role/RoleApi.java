package com.hfhk.auth.service.modules.role;

import com.hfhk.auth.domain.role.Role;
import com.hfhk.auth.domain.role.RoleModifyRequest;
import com.hfhk.auth.domain.role.RolePageFindRequest;
import com.hfhk.auth.domain.role.RoleSaveRequest;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.security.oauth2.user.AuthPrincipal;
import com.hfhk.cairo.starter.service.web.handler.BusinessResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * restful - role
 */
@RestController
@RequestMapping("/Role")
public class RoleApi {
	private final RoleService roleService;

	public RoleApi(RoleService roleService) {
		this.roleService = roleService;
	}

	@PostMapping("/Save")
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	@BusinessResult
	public void save(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody RoleSaveRequest request) {
		String client = principal.getClient();
		roleService.save(client, request);
	}

	@PutMapping("/Modify")
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	@BusinessResult
	public void modify(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody RoleModifyRequest request) {
		String client = principal.getClient();

		roleService.modify(client, request);
	}

	@DeleteMapping("/Delete/{id}")
	@PreAuthorize("isAuthenticated()")
	@BusinessResult
	public void delete(@AuthenticationPrincipal AuthPrincipal principal, @PathVariable String id) {
		String client = principal.getClient();

		roleService.delete(client, id);
	}

	@PostMapping("/Find")
	@PreAuthorize("isAuthenticated()")
	@BusinessResult
	public Page<Role> pageFind(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody RolePageFindRequest request) {
		String client = principal.getClient();

		return roleService.pageFind(client, request);
	}
}
