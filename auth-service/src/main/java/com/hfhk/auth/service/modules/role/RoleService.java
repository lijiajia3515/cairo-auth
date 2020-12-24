package com.hfhk.auth.service.modules.role;

import com.hfhk.auth.domain.role.*;
import com.hfhk.cairo.core.page.Page;

/**
 * 服务 - 角色
 */
public interface RoleService {

	Page<Role> pageFind(String client, RolePageFindRequest request);

	RoleV2 find(String client, String id);

	RoleV2 save(String client, RoleSaveRequest request);

	RoleV2 modify(String client, RoleModifyRequest request);

	RoleV2 delete(String client, String id);
}
