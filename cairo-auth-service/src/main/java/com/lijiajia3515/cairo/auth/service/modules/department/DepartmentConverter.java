package com.lijiajia3515.cairo.auth.service.modules.department;

import com.lijiajia3515.cairo.auth.modules.department.Department;
import com.lijiajia3515.cairo.auth.modules.department.DepartmentTreeNode;
import com.lijiajia3515.cairo.auth.domain.mongo.DepartmentMongo;

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
