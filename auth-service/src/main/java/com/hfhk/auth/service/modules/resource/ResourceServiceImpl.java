package com.hfhk.auth.service.modules.resource;

import com.hfhk.auth.service.constants.AuthConstant;
import com.hfhk.auth.domain.mongo.ResourceMongo;
import com.hfhk.auth.domain.mongo.RoleMongo;
import com.hfhk.auth.domain.mongo.UserMongo;
import com.hfhk.auth.domain.ResourceTreeNode;
import com.hfhk.auth.domain.request.ResourceModifyRequest;
import com.hfhk.auth.domain.request.ResourceMoveRequest;
import com.hfhk.auth.domain.request.ResourceSaveRequest;
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
		Query query = Query.query(Criteria.where(ResourceMongo.Field.Client).is(client));
		query.with(Sort.by(Sort.Order.asc(ResourceMongo.Field.Metadata.Sort)));
		List<ResourceMongo> data = mongoTemplate.find(query, ResourceMongo.class);
		return ResourceConverter.data2tree(data);
	}

	@Override
	public ResourceTreeNode find(String client, String id) {
		return findById(client, id);
	}

	@Override
	public List<ResourceTreeNode> treeFindByUid(String client, String uid) {
		Query userQuery = Query.query(Criteria.where(UserMongo.Field.Uid).is(uid));
		userQuery.fields()
			.include(UserMongo.Field.ClientRoles)
			.include(UserMongo.Field.ClientResources);

		return Optional.ofNullable(mongoTemplate.findOne(userQuery, UserMongo.class))
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
							Query roleQuery = Query.query(Criteria.where(RoleMongo.Field.Code).in(x));
							Stream<String> roleResources = mongoTemplate.find(roleQuery, RoleMongo.class)
								.stream()
								.flatMap(y -> Optional.ofNullable(y.getResources()).stream())
								.flatMap(Collection::stream);
							return Optional.of(Stream.concat(userResources, roleResources)
								.collect(Collectors.toList()))
								.filter(y -> !y.isEmpty())
								.map(y -> mongoTemplate.find(Query.query(Criteria.where(ResourceMongo.Field._ID).in(y)), ResourceMongo.class));

						} else {
							return Optional.of(mongoTemplate.find(Query.query(Criteria.where(ResourceMongo.Field._ID).is(client)), ResourceMongo.class));
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
	public ResourceTreeNode save(String client, ResourceSaveRequest request) {
		ResourceMongo data = ResourceMongo.builder()
			.client(client)
			.parent(Optional.ofNullable(request.getParentId()).orElse(AuthConstant.RESOURCE_TREE_ROOT))
			.type(request.getType())
			.name(request.getName())
			.permissions(request.getPermissions())
			.path(request.getPath())
			.icon(request.getIcon())
			.build();
		data = mongoTemplate.insert(data);
		return findById(client, data.getId());
	}

	@Override
	public ResourceTreeNode modify(String client, ResourceModifyRequest request) {
		ResourceMongo data = ResourceMongo.builder()
			.client(client)
			.id(request.getId())
			.parent(request.getParentId())
			.type(request.getType())
			.name(request.getName())
			.permissions(request.getPermissions())
			.path(request.getPath())
			.icon(request.getIcon())
			.build();
		data = mongoTemplate.save(data);
		return findById(client, data.getId());
	}

	@Override
	public void move(String client, ResourceMoveRequest request) {
		Query query = Query.query(
			Criteria
				.where(ResourceMongo.Field.Client).is(client)
				.and(ResourceMongo.Field._ID).in(request.getIds())
		);

		Update update = Update.update(ResourceMongo.Field.Parent, request.getMovedParentId());
		UpdateResult result = mongoTemplate.upsert(query, update, ResourceMongo.class);

		log.debug("resource move update result: {}", result);
	}

	@Override
	public ResourceTreeNode delete(String client, String id) {
		Query query = Query.query(
			Criteria
				.where(ResourceMongo.Field.Client).is(client)
				.and(ResourceMongo.Field._ID).in(id)
		);

		ResourceMongo resource = mongoTemplate.findAndRemove(query, ResourceMongo.class);

		log.debug("[resource][delete] result: {}", resource);
		return ResourceConverter.data2tree(resource).orElse(null);
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
