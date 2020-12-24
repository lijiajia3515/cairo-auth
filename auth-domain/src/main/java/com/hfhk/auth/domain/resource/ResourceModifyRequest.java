package com.hfhk.auth.domain.resource;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 资源-保存-请求参数
 */
@Data
public class ResourceModifyRequest implements Serializable {
	/**
	 * id
	 */
	private String id;

	/**
	 * 上级 id
	 */
	private String parentId;

	/**
	 * 资源类型
	 */
	private ResourceType type;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 接口权限标识
	 */
	private Set<String> permissions;

	/**
	 * 接口权限标识
	 */
	private String path;

	/**
	 * 接口权限标识
	 */
	private String icon;

}
