package com.hfhk.auth.domain.role;

import com.hfhk.cairo.core.page.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RolePageFindParam {
	@Builder.Default
	private PageRequest page = new PageRequest();

	private String keyword;
}
