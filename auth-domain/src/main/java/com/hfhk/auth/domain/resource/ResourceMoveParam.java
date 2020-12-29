package com.hfhk.auth.domain.resource;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collection;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class ResourceMoveParam {
	/**
	 * 要移动的 id
	 */
	private Collection<String> ids;

	/**
	 * 更新后的 parentId
	 */
	private String movedParentId;
}
