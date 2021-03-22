package com.lijiajia3515.cairo.auth.service.modules.department;

import com.lijiajia3515.cairo.auth.modules.department.*;
import com.lijiajia3515.cairo.core.page.Page;
import com.lijiajia3515.cairo.security.oauth2.user.AuthPrincipal;
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
								 @RequestBody DepartmentFindParam param) {
		String client = principal.getClient();

		return departmentService.find(client, param);
	}

	@PostMapping("/FindPage")
	@PreAuthorize("isAuthenticated()")
	public Page<Department> findPage(@AuthenticationPrincipal AuthPrincipal principal,
									 @RequestBody DepartmentFindParam param) {
		String client = principal.getClient();

		return departmentService.findPage(client, param);
	}

	@PostMapping("/FindTree")
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

	@PutMapping("/Modify")
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
