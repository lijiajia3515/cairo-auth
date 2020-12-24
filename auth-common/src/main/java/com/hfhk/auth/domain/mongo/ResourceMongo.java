package com.hfhk.auth.domain.mongo;

import com.hfhk.cairo.mongo.data.Metadata;
import com.hfhk.cairo.mongo.data.mapping.model.UpperCamelCaseFieldNames;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;

/**
 * 资源
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceMongo implements Serializable {

	/**
	 * id
	 */
	private String id;

	/**
	 * client
	 */
	private String client;

	/**
	 * 上级id
	 */
	private String parent;

	/**
	 * 类型
	 */
	private Type type;

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
	 * 图标
	 */
	private String icon;

	/**
	 * Metadata
	 */
	@Builder.Default
	private Metadata metadata = new Metadata();


	/**
	 * 资源类型
	 */
	public enum Type {
		/**
		 * 菜单
		 */
		MENU,

		/**
		 * 元素
		 */
		ELEMENT;

		public String value = name();
	}

	public static final class Field extends UpperCamelCaseFieldNames {
		public static final String Client = "Client";
		public static final String Parent ="Parent";
		public static final String Type = "Type";
		public static final String Name = "Name";
		public static final String Permissions = "Permissions";
		public static final String Path = "Path";
		public static final String Icon = "Icon";
	}
}
