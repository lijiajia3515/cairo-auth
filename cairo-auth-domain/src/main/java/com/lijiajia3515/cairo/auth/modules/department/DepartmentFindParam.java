package com.lijiajia3515.cairo.auth.modules.department;


import com.lijiajia3515.cairo.core.page.AbstractPage;
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
