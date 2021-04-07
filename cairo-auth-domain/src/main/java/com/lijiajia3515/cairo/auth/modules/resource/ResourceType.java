package com.lijiajia3515.cairo.auth.modules.resource;


import java.io.Serializable;

/**
 * 资源类型
 */
public enum ResourceType implements Serializable {
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
