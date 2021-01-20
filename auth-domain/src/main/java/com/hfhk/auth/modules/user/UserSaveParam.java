package com.hfhk.auth.modules.user;

import com.sun.source.doctree.SeeTree;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveParam implements Serializable {

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
	private Set<String> roleIds;

	/**
	 * 部门
	 */
	private Set<String> departmentIds;

	/**
	 * 资源code
	 */
	private Set<String> resourceIds;
}
