package com.hfhk.auth.service.modules.department;

import com.hfhk.auth.domain.department.DepartmentPageFindRequest;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.auth.domain.mongo.DepartmentMongo;

import java.util.Collection;
import java.util.List;

/**
 * 部门 mongo 模板
 */
public interface DepartmentMongoTemplate {

	/**
	 * 根据 ids
	 *
	 * @param clientId clientId
	 * @param ids      ids
	 * @return 部门 mongo
	 */
	List<DepartmentMongo> findByIds(String clientId, Collection<String> ids);

	/**
	 * 查找
	 *
	 * @param clientId clientId
	 * @return 部门 mongo
	 */
	List<DepartmentMongo> find(String clientId);

	/**
	 * 查找 - 分页
	 *
	 * @param clientId clientId
	 * @return 部门 mongo
	 */
	Page<DepartmentMongo> pageFind(String clientId, DepartmentPageFindRequest request);

}
