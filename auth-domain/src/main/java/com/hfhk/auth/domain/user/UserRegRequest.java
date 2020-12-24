package com.hfhk.auth.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegRequest implements Serializable {

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
	 * 密码
	 */
	private String password;

	/**
	 * 角色code
	 */
	private List<String> roleCodes;

	/**
	 * 部门
	 */
	private List<String> departmentIds;

	/**
	 * 资源code
	 */
	private List<String> resourceIds;
}
