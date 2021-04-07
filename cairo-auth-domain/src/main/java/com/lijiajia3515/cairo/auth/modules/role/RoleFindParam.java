package com.lijiajia3515.cairo.auth.modules.role;

import com.lijiajia3515.cairo.core.page.AbstractPage;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RoleFindParam extends AbstractPage<RoleFindParam> implements Serializable {
	private String keyword;
}
