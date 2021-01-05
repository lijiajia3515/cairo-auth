package com.hfhk.auth.service.modules.department;

import com.hfhk.auth.domain.department.*;
import com.hfhk.cairo.security.oauth2.user.AuthPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Department Api
 */
@RestController
@RequestMapping("/Department")
public class DepartmentApi {

	private final DepartmentService departmentService;

	public DepartmentApi(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@PostMapping("/Find")
	@PreAuthorize("isAuthenticated()")
	public List<Department> find(@AuthenticationPrincipal AuthPrincipal principal,
								 @RequestBody DepartmentFindParam request) {
		String client = principal.getClient();

		return departmentService.find(client, request);
	}

	@PostMapping("/Tree")
	@PreAuthorize("isAuthenticated()")
	public List<DepartmentTreeNode> treeFind(@AuthenticationPrincipal AuthPrincipal principal) {
		String client = principal.getClient();

		return departmentService.treeFind(client);
	}

	@PostMapping("/Save")
	@PreAuthorize("isAuthenticated()")
	public Department save(@AuthenticationPrincipal AuthPrincipal principal,
						   @RequestBody DepartmentSaveParam request) {
		String client = principal.getClient();

		return departmentService.save(client, request).orElseThrow();
	}

	@PutMapping("/Put")
	@PreAuthorize("isAuthenticated()")
	public Department put(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody DepartmentModifyParam request) {
		String client = principal.getClient();

		return departmentService.modify(client, request).orElseThrow();
	}

	@DeleteMapping("/Delete")
	@PreAuthorize("isAuthenticated()")
	public List<Department> delete(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody DepartmentDeleteParam param) {
		String client = principal.getClient();

		return departmentService.delete(client, param);
	}

}
