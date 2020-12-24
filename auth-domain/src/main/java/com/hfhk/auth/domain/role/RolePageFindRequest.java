package com.hfhk.auth.domain.role;

import com.hfhk.cairo.core.request.PageRequest;
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

public class RolePageFindRequest {
	@Builder.Default
	private PageRequest page = new PageRequest();
	private String keyword;
}
