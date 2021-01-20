package com.hfhk.auth.service.modules.department;

import com.hfhk.auth.modules.department.DepartmentTreeNode;

import java.util.Comparator;

public class Constant extends com.hfhk.auth.service.constants.Constant {
	public static final String DEPARTMENT_TREE_ROOT = TREE_ROOT;
	public static final Comparator<DepartmentTreeNode> TREE_COMPARATOR = Comparator.comparing(DepartmentTreeNode::getSort).thenComparing(DepartmentTreeNode::getId);
}
