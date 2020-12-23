package com.hfhk.auth.service.modules.department;

import com.hfhk.auth.domain.Department;
import com.hfhk.auth.domain.DepartmentTreeNode;
import com.hfhk.auth.domain.request.DepartmentFindRequest;
import com.hfhk.auth.domain.request.DepartmentModifyRequest;
import com.hfhk.auth.domain.request.DepartmentSaveRequest;
import com.hfhk.cairo.security.oauth2.server.resource.authentication.CairoAuthenticationToken;
import com.hfhk.cairo.starter.web.handler.BusinessResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * restful - department
 */
@RestController
@RequestMapping("/department")
public class DepartmentApi {

	private final DepartmentService departmentService;

	public DepartmentApi(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@GetMapping
	@BusinessResult
	public List<Department> find(@AuthenticationPrincipal CairoAuthenticationToken token,
								 DepartmentFindRequest request,
								 @PageableDefault Pageable pageable) {
		String client = token.getClient();

		return departmentService.find(client, request, pageable);
	}

	@GetMapping("/tree")
	@BusinessResult
	public List<DepartmentTreeNode> treeFind(@AuthenticationPrincipal CairoAuthenticationToken token) {
		String client = token.getClient();

		return departmentService.treeFind(client);
	}

	@PostMapping
	@BusinessResult
	public Department save(@AuthenticationPrincipal CairoAuthenticationToken token, @RequestBody DepartmentSaveRequest request) {
		String client = token.getClient();

		return departmentService.save(client, request);
	}

	@PutMapping
	@BusinessResult
	public Department put(@AuthenticationPrincipal CairoAuthenticationToken token, @RequestBody DepartmentModifyRequest request) {
		String client = token.getClient();

		return departmentService.modify(client, request);
	}

	@DeleteMapping("/{id}")
	@BusinessResult
	public Department delete(@AuthenticationPrincipal CairoAuthenticationToken token, @PathVariable String id) {
		String client = token.getClient();

		return departmentService.delete(client, id);
	}

}
