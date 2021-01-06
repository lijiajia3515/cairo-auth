package com.hfhk.auth.domain.department;

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
	private String parent;

	/**
	 * 部门名称
	 */
	private String name;
}
