package com.hfhk.auth.server.domain.mongo;

import com.hfhk.auth.server.constants.Mongo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = Mongo.Collection.USER)

@Data
@Accessors(chain = true)

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
	 * 邮箱号码
	 */
	private String email;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 手机号码
	 */
	private String phoneNumber;

	/**
	 * 资源id
	 */
	private List<String> resources;

	/**
	 * 账号 启用
	 */
	private Boolean accountEnabled;

	/**
	 * 账号 锁定
	 */
	private Boolean accountLocked;
}
