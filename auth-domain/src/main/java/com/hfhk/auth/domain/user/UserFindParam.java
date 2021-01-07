package com.hfhk.auth.domain.user;

import com.hfhk.cairo.core.page.AbstractPage;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
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
	private Collection<String> roleIds;

	/**
	 * 用户状态
	 */
	private Collection<Boolean> statuses;

}
