package com.hfhk.auth.server2.modules.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUser implements OAuth2User, UserDetails {
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
	 * 账号 -是否启用
	 */
	private boolean enabled;

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
	private Collection<GrantedAuthority> authorities;

	private Map<String, Object> attributes;

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String getName() {
		return uid;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}
}
