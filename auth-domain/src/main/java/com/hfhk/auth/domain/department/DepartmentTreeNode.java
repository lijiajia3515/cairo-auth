package com.hfhk.auth.domain.department;

import com.hfhk.cairo.core.tree.TreeNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 树节点 - 部门
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentTreeNode implements TreeNode<String, DepartmentTreeNode> {

	/**
	 * id
	 */
	private String id;

	/**
	 * 父级 id
	 */
	private String parent;

	/**
	 * 部门名称
	 */
	private String name;

	/**
	 * 启用
	 */
	private Boolean enabled;

	/**
	 * sort
	 */
	private Long sort;

	/**
	 * 子集
	 */
	@Builder.Default
	private List<DepartmentTreeNode> subs = new ArrayList<>();

	@Override
	public String id() {
		return id;
	}

	@Override
	public String parent() {
		return parent;
	}

	@Override
	public List<DepartmentTreeNode> subs() {
		return subs;
	}
}
