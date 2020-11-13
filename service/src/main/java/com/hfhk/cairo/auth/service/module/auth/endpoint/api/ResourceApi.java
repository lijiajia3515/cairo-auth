package com.hfhk.cairo.auth.service.module.auth.endpoint.api;

import com.hfhk.cairo.auth.domain.ResourceTreeNode;
import com.hfhk.cairo.auth.domain.request.ResourceModifyRequest;
import com.hfhk.cairo.auth.domain.request.ResourceMoveRequest;
import com.hfhk.cairo.auth.domain.request.ResourceSaveRequest;
import com.hfhk.cairo.auth.service.module.auth.service.ResourceService;
import com.hfhk.cairo.security.oauth2.server.resource.authentication.CairoAuthenticationToken;
import com.hfhk.cairo.starter.web.handler.StatusResult;
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
	@StatusResult
	public ResourceTreeNode save(@AuthenticationPrincipal CairoAuthenticationToken token, @RequestBody ResourceSaveRequest request) {
		String client = token.getClient().getId();

		return resourceService.save(client, request);
	}

	@PutMapping
	@StatusResult
	public ResourceTreeNode modify(@AuthenticationPrincipal CairoAuthenticationToken token, @RequestBody ResourceModifyRequest request) {
		String client = token.getClient().getId();

		return resourceService.modify(client, request);
	}

	@PatchMapping("/move")
	@PreAuthorize("isAuthenticated()")
	@StatusResult
	public void move(@AuthenticationPrincipal CairoAuthenticationToken token, @RequestBody ResourceMoveRequest request) {
		String client = token.getClient().getId();

		resourceService.move(client, request);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	@StatusResult
	public ResourceTreeNode delete(@AuthenticationPrincipal CairoAuthenticationToken token, @PathVariable String id) {
		return resourceService.delete(token.getClient().getId(), id);
	}

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	@StatusResult
	public ResourceTreeNode find(@AuthenticationPrincipal CairoAuthenticationToken token, @PathVariable String id) {
		String client = token.getClient().getId();

		return resourceService.find(client, id);
	}

	@GetMapping("/tree")
	@PreAuthorize("isAuthenticated()")
	@StatusResult
	public List<ResourceTreeNode> tree(@AuthenticationPrincipal CairoAuthenticationToken token) {
		String client = token.getClient().getId();

		return resourceService.treeFind(client);
	}


}
