package com.hfhk.auth.service.modules.resource;

import com.hfhk.auth.domain.ResourceTreeNode;
import com.hfhk.auth.domain.request.ResourceModifyRequest;
import com.hfhk.auth.domain.request.ResourceMoveRequest;
import com.hfhk.auth.domain.request.ResourceSaveRequest;

import java.util.List;

/**
 * 资源 服务
 */
public interface ResourceService {
	/**
	 * 全部
	 *
	 * @param client client
	 * @return 资源树 list
	 */
	List<ResourceTreeNode> treeFind(String client);

	/**
	 * find
	 *
	 * @param client client
	 * @param id     id
	 * @return 资源
	 */
	ResourceTreeNode find(String client, String id);


	/**
	 * 根据用户查找
	 *
	 * @param uid uid
	 * @return 资源
	 */
	List<ResourceTreeNode> treeFindByUid(String client, String uid);

	/**
	 * 资源保存
	 *
	 * @param request request
	 * @return 保存后的资源值
	 */
	ResourceTreeNode save(String client, ResourceSaveRequest request);

	/**
	 * 资源修改
	 *
	 * @param client  client
	 * @param request request
	 * @return 修改后的资源值
	 */
	ResourceTreeNode modify(String client, ResourceModifyRequest request);

	/**
	 * 资源 移动
	 *
	 * @param client  client
	 * @param request request
	 */
	void move(String client, ResourceMoveRequest request);

	/**
	 * 删除
	 * @param client client
	 * @param id id
	 * @return 删除
	 */
	ResourceTreeNode delete(String client, String id);
}
