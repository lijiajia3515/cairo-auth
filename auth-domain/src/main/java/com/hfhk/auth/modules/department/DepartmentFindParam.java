package com.hfhk.auth.modules.department;


import com.hfhk.cairo.core.page.AbstractPage;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 部门 保存 参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentFindParam extends AbstractPage<DepartmentFindParam> implements Serializable {
	private String parent;
}
