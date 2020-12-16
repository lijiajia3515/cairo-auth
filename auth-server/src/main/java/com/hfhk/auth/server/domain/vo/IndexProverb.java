package com.hfhk.auth.server.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndexProverb {
	/**
	 * 拥有者名称
	 */
	private String ownerUserName;
	/**
	 * 所属组织名称
	 */
	private String ownerGroupName;
	/**
	 *
	 */
	private String position;

	/**
	 * 谚语
	 */
	private String proverb;
}
