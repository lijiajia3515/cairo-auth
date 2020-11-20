package com.hfhk.cairo.auth.service.module.auth.template.mongo;

import com.hfhk.cairo.auth.service.module.auth.domain.mongo.RoleMongo;

import java.util.Collection;
import java.util.List;

public interface RoleMongoTemplate {

	List<RoleMongo> findByCodes(String clientId, Collection<String> roleCodes);
}
