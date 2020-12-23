package com.hfhk.auth.domain.mongo;

import com.hfhk.cairo.mongo.data.Metadata;
import com.hfhk.cairo.mongo.data.mapping.model.UpperCamelCaseFieldNames;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;


/**
 * 用户
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMongo implements Serializable {

	/**
	 * 标识
	 */
	private String id;

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
	 * 密码
	 */
	private String password;

	/**
	 * 邮箱号码
	 */
	private String email;

	/**
	 * 手机号码
	 */
	private String phoneNumber;

	/**
	 * 头像
	 */
	private String avatarUrl;

	/**
	 * 账号 启用
	 */
	@Builder.Default
	private Boolean accountEnabled = false;

	/**
	 * 账号 锁定
	 */
	@Builder.Default
	private Boolean accountLocked = false;

	/**
	 * 角色
	 */
	private Map<String, Set<String>> clientRoles;

	/**
	 * 部门
	 */
	private Map<String, Set<String>> clientDepartments;

	/**
	 * 资源
	 */
	private Map<String, Set<String>> clientResources;

	/**
	 * 额外权限
	 */
	private Map<String, Set<String>> clientAuthorities;

	/**
	 * 元信息
	 */
	@Builder.Default
	private Metadata metadata = new Metadata();

	public static class Field extends UpperCamelCaseFieldNames {
		public static final String Uid = "Uid";
		public static final String Name = "Name";
		public static final String Username = "Username";
		public static final String Password = "Password";
		public static final String Email = "Email";
		public static final String PhoneNumber = "PhoneNumber";

		public static final String AvatarUrl = "AvatarUrl";

		public static final String AccountEnabled = "AccountEnabled";
		public static final String AccountLocked = "AccountLocked";

		public static final String ClientRoles = "ClientRoles";
		public static final String ClientDepartments = "ClientDepartments";
		public static final String ClientResources = "ClientResources";
	}
}
