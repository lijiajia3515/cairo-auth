package com.hfhk.auth.service.modules.department;

import com.hfhk.auth.domain.department.*;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.security.oauth2.user.AuthPrincipal;
import com.hfhk.cairo.starter.service.web.handler.BusinessResult;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Department
 */
@RestController
@RequestMapping("/Department")
public class DepartmentApi {

	private final DepartmentService departmentService;

	public DepartmentApi(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@GetMapping
	@BusinessResult
	public Page<Department> find(@AuthenticationPrincipal AuthPrincipal principal,
								 @RequestBody DepartmentPageFindRequest request) {
		String client = principal.getClient();

		return departmentService.pageFind(client, request);
	}

	@GetMapping("/Tree")
	@BusinessResult
	public List<DepartmentTreeNode> treeFind(@AuthenticationPrincipal AuthPrincipal principal) {
		String client = principal.getClient();

		return departmentService.treeFind(client);
	}

	@PostMapping("/Save")
	@BusinessResult
	public Department save(@AuthenticationPrincipal AuthPrincipal principal,
						   @RequestBody DepartmentSaveRequest request) {
		String client = principal.getClient();

		return departmentService.save(client, request);
	}

	@PutMapping("/Put")
	@BusinessResult
	public Department put(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody DepartmentModifyRequest request) {
		String client = principal.getClient();

		return departmentService.modify(client, request);
	}

	@DeleteMapping("/Delete/{id}")
	@BusinessResult
	public Department delete(@AuthenticationPrincipal AuthPrincipal principal, @PathVariable String id) {
		String client = principal.getClient();

		return departmentService.delete(client, id);
	}

}
