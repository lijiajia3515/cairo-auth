package com.hfhk.cairo.auth.service.module.auth.endpoint.api;

import com.hfhk.cairo.auth.domain.Department;
import com.hfhk.cairo.auth.domain.DepartmentTreeNode;
import com.hfhk.cairo.auth.domain.request.DepartmentFindRequest;
import com.hfhk.cairo.auth.domain.request.DepartmentModifyRequest;
import com.hfhk.cairo.auth.domain.request.DepartmentSaveRequest;
import com.hfhk.cairo.security.oauth2.server.resource.authentication.CairoAuthenticationToken;
import com.hfhk.cairo.auth.service.module.auth.service.DepartmentService;
import com.hfhk.cairo.starter.web.handler.StatusResult;
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
	@StatusResult
	public List<Department> find(@AuthenticationPrincipal CairoAuthenticationToken token,
								 DepartmentFindRequest request,
								 @PageableDefault Pageable pageable) {
		String client = token.getClient().getId();

		return departmentService.find(client, request, pageable);
	}

	@GetMapping("/tree")
	@StatusResult
	public List<DepartmentTreeNode> treeFind(@AuthenticationPrincipal CairoAuthenticationToken token) {
		String client = token.getClient().getId();

		return departmentService.treeFind(client);
	}

	@PostMapping
	@StatusResult
	public Department save(@AuthenticationPrincipal CairoAuthenticationToken token, @RequestBody DepartmentSaveRequest request) {
		String client = token.getClient().getId();

		return departmentService.save(client, request);
	}

	@PutMapping
	@StatusResult
	public Department put(@AuthenticationPrincipal CairoAuthenticationToken token, @RequestBody DepartmentModifyRequest request) {
		String client = token.getClient().getId();

		return departmentService.modify(client, request);
	}

	@DeleteMapping("/{id}")
	@StatusResult
	public Department delete(@AuthenticationPrincipal CairoAuthenticationToken token, @PathVariable String id) {
		String client = token.getClient().getId();

		return departmentService.delete(client, id);
	}

}
