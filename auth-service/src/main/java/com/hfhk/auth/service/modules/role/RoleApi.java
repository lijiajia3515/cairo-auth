package com.hfhk.auth.service.modules.role;

import com.hfhk.auth.domain.Role;
import com.hfhk.auth.domain.request.RoleFindRequest;
import com.hfhk.auth.domain.request.RoleModifyRequest;
import com.hfhk.auth.domain.request.RoleSaveRequest;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.security.oauth2.server.resource.authentication.CairoAuthenticationToken;
import com.hfhk.cairo.starter.web.handler.BusinessResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * restful - role
 */
@RestController
@RequestMapping("/role")
public class RoleApi {
	private final RoleService roleService;

	public RoleApi(RoleService roleService) {
		this.roleService = roleService;
	}

	@PostMapping
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	// @PreAuthorize("hasAuthority(@ROLE.MODIFY) || hasAnyRole(@ROLE.ROLE, @ROLE_ADMIN)")
	@BusinessResult
	public void save(@AuthenticationPrincipal CairoAuthenticationToken token, @RequestBody RoleSaveRequest request) {
		// token.getClient().getId();
		String client = token.getToken().getAudience().stream().findFirst().orElse(null);
		roleService.save(client, request);
	}

	@PutMapping
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	// @PreAuthorize("hasAuthority(@ROLE.MODIFY) || hasAnyRole(@ROLE.ROLE, @ROLE_ADMIN)")
	@BusinessResult
	public void modify(@AuthenticationPrincipal CairoAuthenticationToken token, @RequestBody RoleModifyRequest request) {
		// token.getClient().getId();
		String client = token.getToken().getAudience().stream().findFirst().orElse(null);

		roleService.modify(client, request);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	// @PreAuthorize("hasAuthority(@ROLE.DELETE) || hasAnyRole(@ROLE.ROLE, @ROLE_ADMIN)")
	@BusinessResult
	public void delete(@AuthenticationPrincipal CairoAuthenticationToken token, @PathVariable String id) {
		// token.getClient().getId();
		String client = token.getToken().getAudience().stream().findFirst().orElse(null);

		roleService.delete(client, id);
	}

	@GetMapping
	@PreAuthorize("isAuthenticated() && #oauth2.isUser()")
	// @PreAuthorize("#oauth2.isClient() && #oauth2.hasScope('resource')")
	// @PreAuthorize("hasAuthority(@ROLE.SAVE) || hasAnyRole(@ROLE.ROLE, @ROLE_ADMIN)")
	@BusinessResult
	public Page<Role> pageFind(@AuthenticationPrincipal CairoAuthenticationToken token, @PageableDefault Pageable pageable, RoleFindRequest request) {
		// token.getClient().getId();
		String client = token.getToken().getAudience().stream().findFirst().orElse(null);

		return roleService.pageFind(client, pageable, request);
	}
}
