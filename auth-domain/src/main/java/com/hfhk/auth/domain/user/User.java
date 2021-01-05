package com.hfhk.auth.domain.user;

import com.hfhk.auth.domain.department.Department;
import com.hfhk.auth.domain.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class User implements Serializable {

	/**
	 * uid
	 */
	private String uid;

	/**
	 * 名称
	 */
	private String name;

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
	 * 头像
	 */
	private String avatarUrl;

	/**
	 * 角色
	 */
	private List<Role> roles;

	/**
	 * 部门
	 */
	private List<Department> departments;

	/**
	 * 最后登录时间
	 */
	private LocalDateTime lastLoginAt;

	/**
	 * 账号 启用
	 */
	private Boolean accountEnabled;

	/**
	 * 账号 锁定
	 */
	private Boolean accountLocked;


}
