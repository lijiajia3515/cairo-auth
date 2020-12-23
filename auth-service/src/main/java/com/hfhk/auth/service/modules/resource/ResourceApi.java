package com.hfhk.auth.service.modules.resource;

import com.hfhk.auth.domain.ResourceTreeNode;
import com.hfhk.auth.domain.request.ResourceModifyRequest;
import com.hfhk.auth.domain.request.ResourceMoveRequest;
import com.hfhk.auth.domain.request.ResourceSaveRequest;
import com.hfhk.cairo.security.oauth2.server.resource.authentication.CairoAuthenticationToken;
import com.hfhk.cairo.starter.web.handler.BusinessResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色 - 资源
 */
@RequestMapping("/resource")
@RestController
public class ResourceApi {

	private final ResourceService resourceService;

	public ResourceApi(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	@BusinessResult
	public ResourceTreeNode save(@AuthenticationPrincipal CairoAuthenticationToken token, @RequestBody ResourceSaveRequest request) {
		String client = token.getClient();

		return resourceService.save(client, request);
	}

	@PutMapping
	@BusinessResult
	public ResourceTreeNode modify(@AuthenticationPrincipal CairoAuthenticationToken token, @RequestBody ResourceModifyRequest request) {
		String client = token.getClient();

		return resourceService.modify(client, request);
	}

	@PatchMapping("/move")
	@PreAuthorize("isAuthenticated()")
	@BusinessResult
	public void move(@AuthenticationPrincipal CairoAuthenticationToken token, @RequestBody ResourceMoveRequest request) {
		String client = token.getClient();

		resourceService.move(client, request);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	@BusinessResult
	public ResourceTreeNode delete(@AuthenticationPrincipal CairoAuthenticationToken token, @PathVariable String id) {
		String client = token.getClient();

		return resourceService.delete(client, id);
	}

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	@BusinessResult
	public ResourceTreeNode find(@AuthenticationPrincipal CairoAuthenticationToken token, @PathVariable String id) {
		String client = token.getClient();

		return resourceService.find(client, id);
	}

	@GetMapping("/tree")
	@PreAuthorize("isAuthenticated()")
	@BusinessResult
	public List<ResourceTreeNode> tree(@AuthenticationPrincipal CairoAuthenticationToken token) {
		String client = token.getClient();

		return resourceService.treeFind(client);
	}


}
