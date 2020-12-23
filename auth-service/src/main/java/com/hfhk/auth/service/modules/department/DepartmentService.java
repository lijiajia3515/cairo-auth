package com.hfhk.auth.service.modules.department;

import com.hfhk.auth.domain.Department;
import com.hfhk.auth.domain.DepartmentTreeNode;
import com.hfhk.auth.domain.request.DepartmentFindRequest;
import com.hfhk.auth.domain.request.DepartmentModifyRequest;
import com.hfhk.auth.domain.request.DepartmentSaveRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 服务 - 部门
 */
public interface DepartmentService {

	/**
	 * 查找
	 *
	 * @param client client
	 * @return 部门查询
	 */
	List<Department> find(String client, DepartmentFindRequest request, Pageable pageable);

	/**
	 * 属性 查询
	 *
	 * @param client client
	 * @return 部门 list
	 */
	List<DepartmentTreeNode> treeFind(String client);

	/**
	 * 部门 - 保存
	 *
	 * @param client  client
	 * @param request request
	 * @return 已保存的部门
	 */
	Department save(String client, DepartmentSaveRequest request);

	/**
	 * 部门 - 休息
	 *
	 * @param client  client
	 * @param request request
	 * @return 已修改的部门
	 */
	Department modify(String client, DepartmentModifyRequest request);

	/**
	 * 部门删除
	 *
	 * @param client client
	 * @param id     部门
	 * @return 已删除 部门
	 */
	Department delete(String client, String id);

}
