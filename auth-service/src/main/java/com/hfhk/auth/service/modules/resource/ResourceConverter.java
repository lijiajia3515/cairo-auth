package com.hfhk.auth.service.modules.resource;

import com.hfhk.auth.domain.mongo.ResourceMongo;
import com.hfhk.auth.domain.resource.ResourceTreeNode;
import com.hfhk.auth.domain.resource.ResourceType;
import com.hfhk.auth.domain.resource.ResourceV1;
import com.hfhk.cairo.core.tree.TreeConverter;
import com.hfhk.cairo.mongo.data.Metadata;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
public class ResourceConverter {

	public static ResourceV1 data2V1(ResourceMongo data) {
		if (data == null) return null;
		ResourceV1 node = new ResourceV1();
		node.setId(data.getId())
			.setParentId(data.getParent())
			.setType(Optional.ofNullable(data.getType()).map(x -> ResourceType.valueOf(x.name())).orElse(null))
			.setName(data.getName())
			.setPermissions(data.getPermissions())
			.setPath(data.getPath())
			.setIcon(data.getIcon())
			.setSort(Optional.ofNullable(data.getMetadata()).map(Metadata::getSort).orElse(0L));
		return node;
	}

	public static Optional<ResourceTreeNode> data2tree(ResourceMongo resource) {
		return Optional.ofNullable(resource)
			.map(data -> new ResourceTreeNode()
				.setId(data.getId())
				.setParentId(data.getParent())
				.setType(Optional.ofNullable(data.getType()).map(x -> ResourceType.valueOf(x.name())).orElse(null))
				.setName(data.getName())
				.setPermissions(data.getPermissions())
				.setPath(data.getPath())
				.setIcon(data.getIcon())
				.setSort(Optional.ofNullable(data.getMetadata()).map(Metadata::getSort).orElse(0L)));

	}

	public static List<ResourceTreeNode> data2tree(List<ResourceMongo> content) {
		List<ResourceTreeNode> resources = content.stream()
			.flatMap(x -> data2tree(x).stream())
			.collect(Collectors.toList());
		return TreeConverter.build(resources, Constant.RESOURCE_ROOT, Comparator.comparing(ResourceTreeNode::getSort).thenComparing(ResourceTreeNode::getId));
	}
}
