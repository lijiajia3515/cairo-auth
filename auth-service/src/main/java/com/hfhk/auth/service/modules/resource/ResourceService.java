package com.hfhk.auth.service.modules.resource;

import com.hfhk.auth.domain.mongo.Mongo;
import com.hfhk.auth.domain.mongo.ResourceMongo;
import com.hfhk.auth.domain.mongo.RoleMongo;
import com.hfhk.auth.domain.mongo.UserMongo;
import com.hfhk.auth.domain.resource.*;
import com.hfhk.auth.service.constants.AuthConstant;
import com.hfhk.cairo.core.auth.RoleConstant;
import com.hfhk.cairo.core.tree.TreeConverter;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hfhk.auth.service.constants.Constant.TREE_ROOT;

/**
 * 资源 服务
 */
@Slf4j
@Service
public class ResourceService {
	private static final String RESOURCE_TREE_ROOT = TREE_ROOT;
	private final Comparator<ResourceTreeNode> RESOURCE_TREE_COMPARATOR = Comparator.comparing(ResourceTreeNode::getSort).thenComparing(ResourceTreeNode::getId);

	private final MongoTemplate mongoTemplate;

	public ResourceService(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	/**
	 * 根据用户查找
	 *
	 * @param uid uid
	 * @return 资源
	 */
	public List<ResourceTreeNode> treeFindByUid(String client, String uid) {
		Query userQuery = Query.query(Criteria.where(UserMongo.FIELD.UID).is(uid));
		userQuery.fields()
			.include(UserMongo.FIELD.CLIENT_ROLES.SELF)
			.include(UserMongo.FIELD.CLIENT_RESOURCES.SELF);

		return Optional.ofNullable(mongoTemplate.findOne(userQuery, UserMongo.class, Mongo.Collection.USER))
			.flatMap(user -> {
				Stream<String> userResources = Optional.ofNullable(user.getClientResources())
					.filter(x -> x.containsKey(client))
					.flatMap(x -> Optional.ofNullable(x.get(client)))
					.stream().flatMap(Collection::stream);

				return Optional.ofNullable(user.getClientRoles())
					.filter(clientRoles -> clientRoles.containsKey(client))
					.flatMap(clientRoles -> Optional.ofNullable(clientRoles.get(client)))
					.filter(roleCodes -> !roleCodes.isEmpty())
					.flatMap(codes -> {
						if (!codes.contains(RoleConstant.ADMIN)) {
							Query roleQuery = Query.query(Criteria.where(RoleMongo.FIELD.CODE).in(codes));
							Stream<String> roleResources = mongoTemplate.find(roleQuery, RoleMongo.class, Mongo.Collection.ROLE)
								.stream()
								.flatMap(y -> Optional.ofNullable(y.getResources()).stream())
								.flatMap(Collection::stream);
							return Optional.of(Stream.concat(userResources, roleResources)
								.collect(Collectors.toList()))
								.filter(y -> !y.isEmpty())
								.map(ids -> mongoTemplate.find(Query.query(Criteria.where(ResourceMongo.FIELD._ID).in(ids)), ResourceMongo.class, Mongo.Collection.RESOURCE));

						} else {
							return Optional.of(mongoTemplate.find(Query.query(Criteria.where(ResourceMongo.FIELD._ID).is(client)), ResourceMongo.class, Mongo.Collection.RESOURCE));
						}
					});
			})
			.filter(x -> !x.isEmpty())
			.map(this::buildTree)
			.orElse(Collections.emptyList());

	}

	/**
	 * 资源保存
	 *
	 * @param param param
	 * @return 保存后的资源值
	 */
	@Transactional(rollbackFor = Exception.class)
	ResourceTreeNode save(String client, ResourceSaveParam param) {
		ResourceMongo data = ResourceMongo.builder()
			.client(client)
			.parent(Optional.ofNullable(param.getParentId()).orElse(AuthConstant.RESOURCE_TREE_ROOT))
			.type(Optional.ofNullable(param.getType()).map(x -> ResourceMongo.Type.valueOf(x.name())).orElse(null))
			.name(param.getName())
			.permissions(param.getPermissions())
			.path(param.getPath())
			.icon(param.getIcon())
			.build();
		data = mongoTemplate.insert(data, Mongo.Collection.RESOURCE);
		return treeFindById(client, data.getId());
	}

	/**
	 * 资源修改
	 *
	 * @param client client
	 * @param param  param
	 * @return 修改后的资源值
	 */
	@Transactional(rollbackFor = Exception.class)
	ResourceTreeNode modify(String client, ResourceModifyParam param) {
		ResourceMongo data = ResourceMongo.builder()
			.client(client)
			.id(param.getId())
			.parent(param.getParentId())
			.type(Optional.ofNullable(param.getType()).map(x -> ResourceMongo.Type.valueOf(x.name())).orElse(null))
			.name(param.getName())
			.permissions(param.getPermissions())
			.path(param.getPath())
			.icon(param.getIcon())
			.build();
		data = mongoTemplate.save(data, Mongo.Collection.RESOURCE);
		return treeFindById(client, data.getId());
	}

	/**
	 * 资源 移动
	 *
	 * @param client client
	 * @param param  param
	 */
	@Transactional(rollbackFor = Exception.class)
	void move(String client, ResourceMoveParam param) {
		Query query = Query.query(
			Criteria
				.where(ResourceMongo.FIELD.CLIENT).is(client)
				.and(ResourceMongo.FIELD._ID).in(param.getIds())
		);

		Update update = Update.update(ResourceMongo.FIELD.PARENT, param.getMovedParentId());
		UpdateResult result = mongoTemplate.upsert(query, update, ResourceMongo.class, Mongo.Collection.RESOURCE);

		log.debug("resource move update result: {}", result);
	}

	/**
	 * 删除
	 *
	 * @param client client
	 * @param param  param
	 * @return 删除
	 */
	@Transactional(rollbackFor = Exception.class)
	List<ResourceTreeNode> delete(String client, ResourceDeleteParam param) {
		Criteria criteria = Criteria.where(ResourceMongo.FIELD.CLIENT).is(client);
		Optional.ofNullable(param.getIds()).ifPresent(ids -> criteria.and(ResourceMongo.FIELD._ID).in(ids));

		Query query = Query.query(criteria);
		List<ResourceTreeNode> deleteResources = mongoTemplate.findAllAndRemove(query, ResourceMongo.class, Mongo.Collection.RESOURCE)
			.stream().flatMap(x -> ResourceConverter.resourceTreeNodeOptional(x).stream()).collect(Collectors.toList());

		log.debug("[resource][delete] result: {}", deleteResources);
		return deleteResources;
	}

	// find

	/**
	 * 全部
	 *
	 * @param client client
	 * @return 资源树 list
	 */
	List<ResourceTreeNode> treeFind(String client) {
		Query query = Query.query(Criteria.where(ResourceMongo.FIELD.CLIENT).is(client));
		query.with(Sort.by(Sort.Order.asc(ResourceMongo.FIELD.METADATA.SORT)));
		List<ResourceMongo> contents = mongoTemplate.find(query, ResourceMongo.class, Mongo.Collection.RESOURCE);

		return buildTree(contents);
	}

	List<ResourceTreeNode> find(String client, ResourceFindParam param) {
		return Collections.emptyList();
	}

	/**
	 * find
	 *
	 * @param client client
	 * @param id     id
	 * @return 资源
	 */
	ResourceTreeNode treeFindById(String client, String id) {
		Set<ResourceMongo> contents = findSubsByIds(client, Collections.singleton(id));
		List<ResourceTreeNode> list = buildTree(contents);

		return list.stream()
			.filter(x -> x.getId().equals(id)).findFirst()
			.map(x -> TreeConverter.build(list, x.getParent(), RESOURCE_TREE_COMPARATOR))
			.filter(x -> !x.isEmpty())
			.map(x -> x.get(0))
			.orElse(null);
	}

	/**
	 * 根据 id 查询 当前id和子集
	 *
	 * @param client client
	 * @param ids    ids
	 * @return 资源 mongo
	 */
	Set<ResourceMongo> findSubsByIds(String client, Collection<String> ids) {
		Query query = Query.query(
			Criteria.where(ResourceMongo.FIELD.CLIENT).is(client)
				.and(ResourceMongo.FIELD._ID).in(ids)
		);
		List<ResourceMongo> firstList = mongoTemplate.find(query, ResourceMongo.class, Mongo.Collection.RESOURCE);
		Set<ResourceMongo> allList = new HashSet<>(firstList);
		findSubs(allList, firstList);

		return allList;
	}


	/**
	 * 根据父级查找 子集
	 *
	 * @param list list
	 */
	void findSubs(Collection<ResourceMongo> data, List<ResourceMongo> list) {
		Optional.of(list.stream().map(ResourceMongo::getId).collect(Collectors.toSet()))
			.filter(x -> !x.isEmpty())
			.ifPresent(parentIds -> {
				List<ResourceMongo> subList = mongoTemplate.find(
					Query.query(
						Criteria.where(ResourceMongo.FIELD._ID).in(parentIds)
					),
					ResourceMongo.class,
					Mongo.Collection.RESOURCE
				);
				data.addAll(subList);
				findSubs(data, subList);
			});
	}

	public List<ResourceTreeNode> buildTree(Collection<ResourceMongo> contents) {
		List<ResourceTreeNode> nodes = contents.stream().map(ResourceConverter::resourceTreeNodeMapper).collect(Collectors.toList());
		return TreeConverter.build(nodes, RESOURCE_TREE_ROOT, RESOURCE_TREE_COMPARATOR);
	}


}
