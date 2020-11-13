package com.hfhk.cairo.auth.service.module.auth.service.impl;

import com.hfhk.cairo.auth.domain.Department;
import com.hfhk.cairo.auth.domain.DepartmentTreeNode;
import com.hfhk.cairo.auth.domain.request.DepartmentFindRequest;
import com.hfhk.cairo.auth.domain.request.DepartmentModifyRequest;
import com.hfhk.cairo.auth.domain.request.DepartmentSaveRequest;
import com.hfhk.cairo.core.tree.TreeConverter;
import com.hfhk.cairo.auth.service.module.auth.domain.mongo.DepartmentMongo;
import com.hfhk.cairo.auth.service.module.auth.service.DepartmentService;
import com.hfhk.cairo.auth.service.module.auth.template.mongo.DepartmentMongoTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 服务 - 部门
 */
@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {
	private final MongoTemplate mongoTemplate;
	private final DepartmentMongoTemplate departmentMongoTemplate;

	public DepartmentServiceImpl(MongoTemplate mongoTemplate, DepartmentMongoTemplate departmentMongoTemplate) {
		this.mongoTemplate = mongoTemplate;
		this.departmentMongoTemplate = departmentMongoTemplate;
	}

	@Override
	public List<Department> find(String client, DepartmentFindRequest request, Pageable pageable) {
		return departmentMongoTemplate.pageFind(client, request, pageable)
			.stream()
			.map(x ->
				Department.builder()
					.id(x.getId())
					.parentId(x.getParentId())
					.name(x.getName())
					.enabled(x.getEnabled())
					.build()
			)
			.collect(Collectors.toList());
	}

	@Override
	public List<DepartmentTreeNode> treeFind(String client) {
		List<DepartmentTreeNode> nodes = departmentMongoTemplate.find(client)
			.stream()
			.map(x ->
				DepartmentTreeNode.builder()
					.id(x.getId())
					.parentId(x.getParentId())
					.name(x.getName())
					.enabled(x.getEnabled())
					.sort(x.getMetadata().getSort())
					.build()
			)
			.collect(Collectors.toList());
		return TreeConverter.build(nodes, "0", Comparator.comparing(DepartmentTreeNode::getSort).thenComparing(DepartmentTreeNode::getId));
	}

	@Override
	public Department save(String client, DepartmentSaveRequest request) {
		DepartmentMongo mongo = DepartmentMongo.builder()
			.client(client)
			.parentId(request.getParentId())
			.name(request.getName())
			.enabled(true)
			.build();
		mongo.getMetadata().setSort(LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8)));

		mongo = mongoTemplate.insert(mongo);

		return Department.builder()
			.id(mongo.getId())
			.parentId(mongo.getParentId())
			.name(mongo.getName())
			.enabled(mongo.getEnabled())
			.build();
	}

	@Override
	public Department modify(String client, DepartmentModifyRequest request) {
		mongoTemplate.updateFirst(
			Query.query(
				Criteria.where("_id").is(request.getId())
					.and("clientId").is(client)
			),
			Update.update("parentId", request.getParentId())
				.set("name", request.getName()),
			DepartmentMongo.class
		);

		return Optional.ofNullable(mongoTemplate.findById(request.getId(), DepartmentMongo.class))
			.map(mongo -> Department.builder()
				.id(mongo.getId())
				.parentId(mongo.getParentId())
				.name(mongo.getName())
				.enabled(mongo.getEnabled())
				.build())
			.orElse(null);
	}

	@Override
	public Department delete(String client, String id) {
		DepartmentMongo mongo = mongoTemplate.findAndRemove(Query.query(Criteria.where("_id").is(id).and("clientId").is(client)), DepartmentMongo.class);

		return Optional.ofNullable(mongo)
			.map(data -> Department.builder()
				.id(data.getId())
				.parentId(data.getParentId())
				.name(data.getName())
				.enabled(data.getEnabled())
				.build())
			.orElse(null);
	}
}
