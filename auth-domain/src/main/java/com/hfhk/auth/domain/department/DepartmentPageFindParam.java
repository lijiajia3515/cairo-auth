package com.hfhk.auth.domain.department;


import com.hfhk.cairo.core.page.AbstractPageRequest;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 部门 保存 请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentPageFindParam extends AbstractPageRequest<DepartmentPageFindParam> implements Serializable {

	private String parent;
}
