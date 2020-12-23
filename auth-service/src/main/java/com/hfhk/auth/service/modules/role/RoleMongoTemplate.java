package com.hfhk.auth.service.modules.role;

import com.hfhk.auth.domain.mongo.RoleMongo;

import java.util.Collection;
import java.util.List;

public interface RoleMongoTemplate {

	List<RoleMongo> findByCodes(String clientId, Collection<String> roleCodes);
}
