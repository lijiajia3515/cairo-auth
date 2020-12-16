package com.hfhk.auth.service.auth.template.mongo;

import com.hfhk.auth.service.auth.domain.mongo.RoleMongo;

import java.util.Collection;
import java.util.List;

public interface RoleMongoTemplate {

	List<RoleMongo> findByCodes(String clientId, Collection<String> roleCodes);
}
