package com.lijiajia3515.cairo.auth.service.modules.role;

import com.lijiajia3515.cairo.core.exception.UnknownBusinessException;
import com.lijiajia3515.cairo.core.page.Page;
import com.lijiajia3515.cairo.security.oauth2.user.AuthPrincipal;
import com.lijiajia3515.cairo.auth.modules.role.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	@PreAuthorize("isAuthenticated()")
	public RoleV2 save(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody RoleSaveParam param) {
		String client = principal.getClient();
		return roleService.save(client, param);
	}

	@PutMapping("/Modify")
	@PreAuthorize("isAuthenticated()")
	public RoleV2 modify(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody RoleModifyParam param) {
		String client = principal.getClient();

		return roleService.modify(client, param).orElseThrow(() -> new UnknownBusinessException("id not found"));
	}

	@DeleteMapping("/Delete")
	@PreAuthorize("isAuthenticated()")
	public List<RoleV2> delete(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody RoleDeleteParam param) {
		String client = principal.getClient();

		return roleService.delete(client, param);
	}

	// Find
	@PostMapping("/Find")
	@PreAuthorize("isAuthenticated()")
	public List<Role> find(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody RoleFindParam param) {
		String client = principal.getClient();

		return roleService.find(client, param);
	}

	@PostMapping("/FindPage")
	@PreAuthorize("isAuthenticated()")
	public Page<Role> findPage(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody RoleFindParam param) {
		String client = principal.getClient();

		return roleService.pageFind(client, param);
	}

}
