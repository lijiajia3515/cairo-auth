package com.hfhk.auth.service.modules.role;

import com.hfhk.auth.domain.role.*;
import com.hfhk.cairo.core.page.Page;

import java.util.List;

/**
 * 服务 - 角色
 */
public interface RoleService {

	RoleV2 save(String client, RoleSaveParam request);

	RoleV2 modify(String client, RoleModifyParam request);
	List<RoleV2> delete(String client, RoleDeleteParam param);

	Page<Role> pageFind(String client, RolePageFindParam request);

	RoleV2 find(String client, String id);

}
