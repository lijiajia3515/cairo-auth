package com.hfhk.auth.service.modules.resource;

import com.hfhk.auth.domain.resource.ResourceTreeNode;
import com.hfhk.auth.domain.resource.ResourceModifyRequest;
import com.hfhk.auth.domain.resource.ResourceMoveRequest;
import com.hfhk.auth.domain.resource.ResourceSaveRequest;
import com.hfhk.cairo.security.oauth2.server.resource.authentication.CairoAuthentication;
import com.hfhk.cairo.starter.web.handler.BusinessResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色 - 资源
 */
@RequestMapping("/Resource")
@RestController
public class ResourceApi {

	private final ResourceService resourceService;

	public ResourceApi(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@PostMapping("/Save")
	@PreAuthorize("isAuthenticated()")
	@BusinessResult
	public ResourceTreeNode save(@AuthenticationPrincipal CairoAuthentication auth, @RequestBody ResourceSaveRequest request) {
		String client = auth.getToken().getAudience().stream().findFirst().orElse("default");

		return resourceService.save(client, request);
	}

	@PutMapping("/Modify")
	@BusinessResult
	public ResourceTreeNode modify(@AuthenticationPrincipal CairoAuthentication auth, @RequestBody ResourceModifyRequest request) {
		String client = auth.getToken().getAudience().stream().findFirst().orElse("default");

		return resourceService.modify(client, request);
	}

	@PatchMapping("/Move")
	@PreAuthorize("isAuthenticated()")
	@BusinessResult
	public void move(@AuthenticationPrincipal CairoAuthentication auth, @RequestBody ResourceMoveRequest request) {
		String client = auth.getToken().getAudience().stream().findFirst().orElse("default");

		resourceService.move(client, request);
	}

	@DeleteMapping("/Delete/{id}")
	@PreAuthorize("isAuthenticated()")
	@BusinessResult
	public ResourceTreeNode delete(@AuthenticationPrincipal CairoAuthentication auth, @PathVariable String id) {
		String client = auth.getToken().getAudience().stream().findFirst().orElse("default");

		return resourceService.delete(client, id);
	}

	@PostMapping("/Find/{id}")
	@PreAuthorize("isAuthenticated()")
	@BusinessResult
	public ResourceTreeNode find(@AuthenticationPrincipal CairoAuthentication auth, @PathVariable String id) {
		String client = auth.getToken().getAudience().stream().findFirst().orElse("default");

		return resourceService.find(client, id);
	}

	@GetMapping("/Tree")
	@PreAuthorize("isAuthenticated()")
	@BusinessResult
	public List<ResourceTreeNode> tree(@AuthenticationPrincipal CairoAuthentication auth) {
		String client = auth.getToken().getAudience().stream().findFirst().orElse("default");

		return resourceService.treeFind(client);
	}


}
