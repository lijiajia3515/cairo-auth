package com.hfhk.auth.service.modules.resource;

import com.hfhk.auth.domain.resource.*;

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
	 * @param param param
	 * @return 保存后的资源值
	 */
	ResourceTreeNode save(String client, ResourceSaveParam param);

	/**
	 * 资源修改
	 *
	 * @param client  client
	 * @param param param
	 * @return 修改后的资源值
	 */
	ResourceTreeNode modify(String client, ResourceModifyParam param);

	/**
	 * 资源 移动
	 *
	 * @param client  client
	 * @param param param
	 */
	void move(String client, ResourceMoveParam param);

	/**
	 * 删除
	 *
	 * @param client client
	 * @param param     param
	 * @return 删除
	 */
	List<ResourceTreeNode> delete(String client, ResourceDeleteParam param);
}
