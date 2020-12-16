package com.hfhk.cairo.auth.service.module.auth.domain.mongo;

import com.hfhk.cairo.auth.service.module.auth.constants.Mongo;
import com.hfhk.cairo.mongo.data.Metadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = Mongo.Collection.USER)

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMongo {

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
	private Boolean accountEnabled;

	/**
	 * 账号 锁定
	 */
	private Boolean accountLocked;

	/**
	 * 角色
	 */
	private Map<String, List<String>> clientRoles;

	/**
	 * 部门
	 */
	private Map<String, List<String>> clientDepartments;

	/**
	 * 资源
	 */
	private Map<String, List<String>> clientResources;

	/**
	 * 元信息
	 */
	@Builder.Default
	private Metadata metadata = new Metadata();
}
