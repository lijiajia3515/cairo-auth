package com.hfhk.auth.domain.role;

import com.hfhk.cairo.core.page.AbstractPage;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RoleFindParam extends AbstractPage<RoleFindParam> {
	private String keyword;
}
