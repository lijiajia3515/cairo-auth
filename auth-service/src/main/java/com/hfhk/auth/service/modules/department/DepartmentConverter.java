package com.hfhk.auth.service.modules.department;

import com.hfhk.auth.domain.department.Department;
import com.hfhk.auth.domain.department.DepartmentTreeNode;
import com.hfhk.auth.domain.mongo.DepartmentMongo;

import java.util.Optional;

public class DepartmentConverter {

	public static Department departmentMapper(DepartmentMongo data) {
		return Department.builder()
			.id(data.getId())
			.name(data.getName())
			.build();
	}

	public static Optional<Department> departmentOptional(DepartmentMongo data) {
		return Optional.ofNullable(data).map(DepartmentConverter::departmentMapper);
	}

	public static DepartmentTreeNode departmentTreeNodeMapper(DepartmentMongo data) {
		return DepartmentTreeNode.builder()
			.id(data.getId())
			.parent(data.getParent())
			.name(data.getName())
			.sort(data.getMetadata().getSort())
			.build();
	}

	public static Optional<DepartmentTreeNode> departmentTreeNodeOptional(DepartmentMongo data) {
		return Optional.ofNullable(data).map(DepartmentConverter::departmentTreeNodeMapper);
	}

}
