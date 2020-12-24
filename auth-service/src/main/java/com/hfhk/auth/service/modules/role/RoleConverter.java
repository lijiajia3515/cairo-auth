package com.hfhk.auth.service.modules.role;

import com.hfhk.auth.domain.mongo.RoleMongo;
import com.hfhk.auth.domain.resource.ResourceTreeNode;
import com.hfhk.auth.domain.role.Role;
import com.hfhk.auth.domain.role.RoleV2;

import java.util.List;
import java.util.Optional;

public class RoleConverter {
	public static Optional<Role> data2V1(RoleMongo data) {
		return Optional.ofNullable(data)
			.map(x ->
				Role.builder()
					.code(x.getCode())
					.name(x.getName())
					.build()
			);
	}

	public static Optional<RoleV2> data2V2(RoleMongo data, List<ResourceTreeNode> resources) {
		return Optional.ofNullable(data)
			.map(x ->
				RoleV2.builder()
					.code(x.getCode())
					.name(x.getName())
					.resources(resources)
					.build()
			);
	}
}
