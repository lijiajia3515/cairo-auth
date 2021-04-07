package com.lijiajia3515.cairo.auth.modules.department;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 部门
 */
@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department implements Serializable {

	/**
	 * id
	 */
	private String id;

	/**
	 * Parent Id
	 */
	private String parent;

	/**
	 * 部门名称
	 */
	private String name;
}
