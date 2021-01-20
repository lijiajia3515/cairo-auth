package com.hfhk.auth.modules.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;

/**
 * 资源(单个详情)
 */
@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceV1 implements Serializable {
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
}
