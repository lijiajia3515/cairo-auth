package com.hfhk.auth.service.modules.role;

import com.hfhk.auth.domain.Role;
import com.hfhk.auth.domain.RoleV2;
import com.hfhk.auth.domain.request.RoleFindRequest;
import com.hfhk.auth.domain.request.RoleModifyRequest;
import com.hfhk.auth.domain.request.RoleSaveRequest;
import com.hfhk.cairo.core.page.Page;
import org.springframework.data.domain.Pageable;

/**
 * 服务 - 角色
 */
public interface RoleService {

	Page<Role> pageFind(String client, Pageable pageable, RoleFindRequest request);

	RoleV2 find(String client, String id);

	RoleV2 save(String client, RoleSaveRequest request);

	RoleV2 modify(String client, RoleModifyRequest request);

	RoleV2 delete(String client, String id);
}
