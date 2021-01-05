package com.hfhk.auth.domain.user;

import com.hfhk.cairo.core.page.AbstractPageRequest;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPageFindParam extends AbstractPageRequest<UserFindParam> implements Serializable {
	
	private String keyword;

	private Collection<String> uids;
}
