package com.hfhk.auth.domain.user;

import com.hfhk.cairo.core.page.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPageFindParam implements Serializable {

	@Builder.Default
	private PageRequest page = new PageRequest();

	private String keyword;
}
