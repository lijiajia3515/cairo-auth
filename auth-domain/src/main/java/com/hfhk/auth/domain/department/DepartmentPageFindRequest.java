package com.hfhk.auth.domain.department;


import com.hfhk.cairo.core.request.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 部门 保存 请求
 */
@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentPageFindRequest implements Serializable {
	@Builder.Default
	private PageRequest page = new PageRequest();

	private String parent;
}
