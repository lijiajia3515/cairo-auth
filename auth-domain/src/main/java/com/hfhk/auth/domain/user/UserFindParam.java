package com.hfhk.auth.domain.user;

import com.hfhk.cairo.core.page.AbstractPage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFindParam extends AbstractPage<UserFindParam> implements Serializable {

	/**
	 * 关键字
	 */
	private String keyword;

	/**
	 * 用户标识
	 */
	private Collection<String> uids;

	/**
	 * 角色编码
	 */
	private Collection<String> roleCodes;

	/**
	 * 用户状态
	 */
	private Collection<Boolean> enabled;

}
