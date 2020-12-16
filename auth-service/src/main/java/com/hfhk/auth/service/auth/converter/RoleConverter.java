package com.hfhk.auth.service.auth.converter;

import com.hfhk.cairo.auth.domain.ResourceTreeNode;
import com.hfhk.cairo.auth.domain.Role;
import com.hfhk.cairo.auth.domain.RoleV2;
import com.hfhk.auth.service.auth.domain.mongo.RoleMongo;

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
