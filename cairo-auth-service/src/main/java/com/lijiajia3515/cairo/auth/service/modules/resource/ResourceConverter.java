package com.lijiajia3515.cairo.auth.service.modules.resource;

import com.lijiajia3515.cairo.auth.domain.mongo.ResourceMongo;
import com.lijiajia3515.cairo.auth.modules.resource.ResourceTreeNode;
import com.lijiajia3515.cairo.auth.modules.resource.ResourceType;
import com.lijiajia3515.cairo.auth.modules.resource.ResourceV1;
import com.lijiajia3515.cairo.mongo.data.Metadata;

import java.util.Optional;

/**
 *
 */
public class ResourceConverter {

	public static ResourceV1 resourceV1Mapper(ResourceMongo data) {
		return ResourceV1.builder()
			.id(data.getId())
			.parent(data.getParent())
			.type(Optional.ofNullable(data.getType()).map(x -> ResourceType.valueOf(x.name())).orElse(null))
			.name(data.getName())
			.permissions(data.getPermissions())
			.path(data.getPath())
			.icon(data.getIcon())
			.sort(Optional.ofNullable(data.getMetadata()).map(Metadata::getSort).orElse(0L))
			.build();
	}

	public static ResourceTreeNode resourceTreeNodeMapper(ResourceMongo data) {
		return ResourceTreeNode.builder()
			.id(data.getId())
			.parent(data.getParent())
			.type(Optional.ofNullable(data.getType()).map(x -> ResourceType.valueOf(x.name())).orElse(null))
			.name(data.getName())
			.permissions(data.getPermissions())
			.path(data.getPath())
			.icon(data.getIcon())
			.sort(Optional.ofNullable(data.getMetadata()).map(Metadata::getSort).orElse(0L))
			.build();
	}

	public static Optional<ResourceTreeNode> resourceTreeNodeOptional(ResourceMongo resource) {
		return Optional.ofNullable(resource)
			.map(ResourceConverter::resourceTreeNodeMapper);
	}
}
