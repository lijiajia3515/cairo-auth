package com.hfhk.auth.domain.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 角色 V1
 */
@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements Serializable {

	/**
	 * id
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

}
