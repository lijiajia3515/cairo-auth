package com.hfhk.auth.domain.resource;

import com.hfhk.cairo.core.tree.TreeNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 树节点 - 资源
 */
@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceTreeNode implements TreeNode<String, ResourceTreeNode> {
	/**
	 * id
	 */
	private String id;

	/**
	 * 上级id
	 */
	private String parent;

	/**
	 * 类型
	 */
	private ResourceType type;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 接口所需要的权限标识
	 */
	private Set<String> permissions;

	/**
	 * 前端路径/前端路由/页面地址外部地址
	 */
	private String path;

	/**
	 * icon
	 */
	private String icon;

	/**
	 * 排序值
	 */
	private Long sort;

	@Builder.Default
	private List<ResourceTreeNode> subs = new ArrayList<>(1);

	@Override
	public String id() {
		return id;
	}

	@Override
	public String parent() {
		return parent;
	}

	@Override
	public List<ResourceTreeNode> subs() {
		return subs;
	}
}
