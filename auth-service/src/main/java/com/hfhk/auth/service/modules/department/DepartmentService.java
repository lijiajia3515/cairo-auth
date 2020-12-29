package com.hfhk.auth.service.modules.department;

import com.hfhk.auth.domain.department.*;
import com.hfhk.cairo.core.page.Page;

import java.util.List;

/**
 * 服务 - 部门
 */
public interface DepartmentService {

	/**
	 * 部门 - 保存
	 *
	 * @param client  client
	 * @param param param
	 * @return 已保存的部门
	 */
	Department save(String client, DepartmentSaveParam param);

	/**
	 * 部门 - 休息
	 *
	 * @param client  client
	 * @param param param
	 * @return 已修改的部门
	 */
	Department modify(String client, DepartmentModifyParam param);

	/**
	 * 部门删除
	 *
	 * @param client client
	 * @param param     部门
	 * @return 已删除 部门
	 */
	Department delete(String client, DepartmentDeleteParam param);


	/**
	 * 查找
	 *
	 * @param client client
	 * @return 部门查询
	 */
	Page<Department> pageFind(String client, DepartmentPageFindParam request);

	/**
	 * 属性 查询
	 *
	 * @param client client
	 * @return 部门 list
	 */
	List<DepartmentTreeNode> treeFind(String client);

}
