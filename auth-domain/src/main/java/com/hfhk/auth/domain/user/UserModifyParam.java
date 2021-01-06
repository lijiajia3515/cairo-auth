package com.hfhk.auth.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModifyParam implements Serializable {

	/**
	 * uid
	 */
	private String uid;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 手机号
	 */
	private String phoneNumber;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 角色code
	 */
	private List<String> roleIds;

	/**
	 * 部门
	 */
	private List<String> departmentIds;

	/**
	 * 资源code
	 */
	private List<String> resourceIds;
}
