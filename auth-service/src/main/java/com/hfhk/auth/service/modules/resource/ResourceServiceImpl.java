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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 服务实现 - 资源
 */
@Slf4j
@Service
public class ResourceServiceImpl implements ResourceService {
	private final MongoTemplate mongoTemplate;
	private final ResourceMongoTemplate resourceMongoTemplate;

	public ResourceServiceImpl(MongoTemplate mongoTemplate, ResourceMongoTemplate resourceMongoTemplate) {
		this.mongoTemplate = mongoTemplate;
		this.resourceMongoTemplate = resourceMongoTemplate;
	}

	@Override
	public List<ResourceTreeNode> treeFind(String client) {
		Query query = Query.query(Criteria.where(ResourceMongo.FIELD.CLIENT).is(client));
		query.with(Sort.by(Sort.Order.asc(ResourceMongo.FIELD.METADATA.SORT)));
		List<ResourceMongo> data = mongoTemplate.find(query, ResourceMongo.class, Mongo.Collection.RESOURCE);
		return ResourceConverter.data2tree(data);
	}

	@Override
	public ResourceTreeNode find(String client, String id) {
		return findById(client, id);
	}

	@Override
	public List<ResourceTreeNode> treeFindByUid(String client, String uid) {
		Query userQuery = Query.query(Criteria.where(UserMongo.FIELD.UID).is(uid));
		userQuery.fields()
			.include(UserMongo.FIELD.CLIENT_ROLES)
			.include(UserMongo.FIELD.CLIENT_RESOURCES);

		return Optional.ofNullable(mongoTemplate.findOne(userQuery, UserMongo.class, Mongo.Collection.USER))
			.map(user -> {
				Stream<String> userResources = Optional.ofNullable(user.getClientResources())
					.filter(x -> x.containsKey(client))
					.flatMap(x -> Optional.ofNullable(x.get(client)))
					.stream().flatMap(Collection::stream);

				return Optional.ofNullable(user.getClientRoles())
					.filter(x -> x.containsKey(client))
					.flatMap(x -> Optional.ofNullable(x.get(client)).filter(y -> !y.isEmpty()))
					.flatMap(x -> {
						if (!x.contains(RoleConstant.ADMIN)) {
							Query roleQuery = Query.query(Criteria.where(RoleMongo.FIELD.CODE).in(x));
							Stream<String> roleResources = mongoTemplate.find(roleQuery, RoleMongo.class, Mongo.Collection.ROLE)
								.stream()
								.flatMap(y -> Optional.ofNullable(y.getResources()).stream())
								.flatMap(Collection::stream);
							return Optional.of(Stream.concat(userResources, roleResources)
								.collect(Collectors.toList()))
								.filter(y -> !y.isEmpty())
								.map(y -> mongoTemplate.find(Query.query(Criteria.where(ResourceMongo.FIELD._ID).in(y)), ResourceMongo.class, Mongo.Collection.RESOURCE));

						} else {
							return Optional.of(mongoTemplate.find(Query.query(Criteria.where(ResourceMongo.FIELD._ID).is(client)), ResourceMongo.class, Mongo.Collection.RESOURCE));
						}
					})
					.stream()
					.filter(x -> !x.isEmpty())
					.map(ResourceConverter::data2tree)
					.flatMap(Collection::stream)
					.collect(Collectors.toList());
			})
			.orElse(Collections.emptyList());
	}

	@Override
	public ResourceTreeNode save(String client, ResourceSaveParam param) {
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
		return findById(client, data.getId());
	}

	@Override
	public ResourceTreeNode modify(String client, ResourceModifyParam param) {
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
		return findById(client, data.getId());
	}

	@Override
	public void move(String client, ResourceMoveParam param) {
		Query query = Query.query(
			Criteria
				.where(ResourceMongo.FIELD.CLIENT).is(client)
				.and(ResourceMongo.FIELD._ID).in(param.getIds())
		);

		Update update = Update.update(ResourceMongo.FIELD.PARENT, param.getMovedParentId());
		UpdateResult result = mongoTemplate.upsert(query, update, ResourceMongo.class, Mongo.Collection.RESOURCE);

		log.debug("resource move update result: {}", result);
	}

	@Override
	public List<ResourceTreeNode> delete(String client, ResourceDeleteParam param) {
		Criteria criteria = Criteria.where(ResourceMongo.FIELD.CLIENT).is(client);
		Optional.ofNullable(param.getIds()).ifPresent(ids -> criteria.and(ResourceMongo.FIELD._ID).in(ids));

		Query query = Query.query(criteria);
		List<ResourceTreeNode> deleteResources = mongoTemplate.findAllAndRemove(query, ResourceMongo.class, Mongo.Collection.RESOURCE)
			.stream().flatMap(x -> ResourceConverter.data2tree(x).stream()).collect(Collectors.toList());

		log.debug("[resource][delete] result: {}", deleteResources);
		return deleteResources;
	}

	public ResourceTreeNode findById(String client, String id) {
		Comparator<ResourceTreeNode> comparator = Comparator.comparing(ResourceTreeNode::getSort)
			.thenComparing(ResourceTreeNode::getId);
		List<ResourceMongo> data = resourceMongoTemplate.findSubsByIds(client, Collections.singleton(id));
		List<ResourceTreeNode> list = ResourceConverter.data2tree(data);

		return list.stream().filter(x -> x.getId().equals(id)).findFirst()
			.map(x -> TreeConverter.build(list, x.getParentId(), comparator))
			.filter(x -> !x.isEmpty())
			.map(x -> x.get(0))
			.orElse(null);
	}

}
