package com.hfhk.auth.modules.resource;


/**
 * 资源类型
 */
public enum ResourceType {
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
