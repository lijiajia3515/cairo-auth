package com.lijiajia3515.cairo.auth.service.modules.resource;

import com.lijiajia3515.cairo.auth.modules.resource.*;
import com.lijiajia3515.cairo.core.exception.UnknownBusinessException;
import com.lijiajia3515.cairo.security.oauth2.user.AuthPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Resource Api
 */
@RequestMapping("/Resource")
@RestController
public class ResourceApi {

	private final ResourceService resourceService;

	public ResourceApi(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@GetMapping("/Current")
	@PreAuthorize("isAuthenticated()")
	public List<ResourceTreeNode> userResources(@AuthenticationPrincipal AuthPrincipal principal) {
		String client = principal.getClient();

		return resourceService.treeFindByUid(client, principal.getUser().getUid());
	}

	//Operate
	@PostMapping("/Save")
	@PreAuthorize("isAuthenticated()")
	public ResourceTreeNode save(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ResourceSaveParam param) {
		String client = principal.getClient();

		return resourceService.save(client, param);
	}

	@PutMapping("/Modify")
	public ResourceTreeNode modify(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ResourceModifyParam param) {
		String client = principal.getClient();

		return resourceService.modify(client, param);
	}

	@PatchMapping("/Move")
	@PreAuthorize("isAuthenticated()")
	public void move(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ResourceMoveParam param) {
		String client = principal.getClient();

		resourceService.move(client, param);
	}

	@DeleteMapping("/Delete")
	@PreAuthorize("isAuthenticated()")
	public List<ResourceTreeNode> delete(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ResourceDeleteParam param) {
		String client = principal.getClient();

		return resourceService.delete(client, param);
	}

	//Find
	@PostMapping("/Find")
	@PreAuthorize("isAuthenticated()")
	public List<ResourceTreeNode> find(@AuthenticationPrincipal AuthPrincipal principal, ResourceFindParam param) {
		String client = principal.getClient();
		return resourceService.find(client, param);
	}

	@PostMapping("/FindTree")
	@PreAuthorize("isAuthenticated()")
	public List<ResourceTreeNode> tree(@AuthenticationPrincipal AuthPrincipal principal) {
		String client = principal.getClient();

		return resourceService.treeFind(client);
	}

	@PostMapping("/FindById")
	@PreAuthorize("isAuthenticated()")
	public ResourceTreeNode find(@AuthenticationPrincipal AuthPrincipal principal, @RequestParam String id) {
		String client = principal.getClient();

		return resourceService.treeFindById(client, id).orElseThrow(() -> new UnknownBusinessException("111"));
	}


}
