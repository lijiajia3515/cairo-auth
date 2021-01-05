package com.hfhk.auth.server2.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

/**
 * 认证用户
 */
@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUser implements UserDetails {

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
	 * 密码
	 */
	private String password;


	/**
	 * 头像
	 */
	private String avatarUrl;

	/**
	 * 最后登录
	 */
	private LocalDateTime lastLoginAt;

	/**
	 * 账号 -是否启用
	 */
	private boolean accountEnabled;

	/**
	 * 账号是否锁定
	 */
	private boolean accountLocked;

	/**
	 * 角色
	 */
	private Set<String> roles;

	/**
	 * 部门
	 */
	private Set<String> departments;

	/**
	 * 权限值
	 */
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public String getUsername() {
		return uid;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !accountLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return accountEnabled;
	}
}
