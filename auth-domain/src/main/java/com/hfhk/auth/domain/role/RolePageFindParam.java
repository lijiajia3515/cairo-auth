package com.hfhk.auth.domain.role;

import com.hfhk.cairo.core.page.AbstractPageRequest;
import com.hfhk.cairo.core.page.PageRequest;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RolePageFindParam extends AbstractPageRequest<RolePageFindParam> {

	private String keyword;
}
