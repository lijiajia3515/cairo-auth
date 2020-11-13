package com.hfhk.cairo.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 部门
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

	/**
	 * id
	 */
	private String id;

	/**
	 * Parent Id
	 */
	private String parentId;

	/**
	 * 部门名称
	 */
	private String name;

	/**
	 * 启用
	 */
	private Boolean enabled;
}
