package com.hfhk.auth.domain.role;

import com.hfhk.auth.domain.resource.ResourceTreeNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 角色(带资源信息) 详细
 */
@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleV3 implements Serializable {
	/**
	 * id
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 资源树形菜单
	 */
	private List<ResourceTreeNode> resources;
}
