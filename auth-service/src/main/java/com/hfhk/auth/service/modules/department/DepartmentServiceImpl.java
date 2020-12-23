package com.hfhk.auth.service.modules.department;

import com.hfhk.auth.domain.mongo.DepartmentMongo;
import com.hfhk.auth.domain.Department;
import com.hfhk.auth.domain.DepartmentTreeNode;
import com.hfhk.auth.domain.request.DepartmentFindRequest;
import com.hfhk.auth.domain.request.DepartmentModifyRequest;
import com.hfhk.auth.domain.request.DepartmentSaveRequest;
import com.hfhk.cairo.core.tree.TreeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
					.parent(x.getParent())
					.name(x.getName())
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
					.parentId(x.getParent())
					.name(x.getName())
					.sort(x.getMetadata().getSort())
					.build()
			)
			.collect(Collectors.toList());
		return TreeConverter.build(nodes, Constant.DEPARTMENT_TREE_ROOT, Constant.TREE_COMPARATOR);
	}

	@Override
	public Department save(String client, DepartmentSaveRequest request) {
		DepartmentMongo mongo = DepartmentMongo.builder()
			.client(client)
			.parent(request.getParentId())
			.name(request.getName())
			.build();
		mongo.getMetadata().setSort(LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8)));

		mongo = mongoTemplate.insert(mongo);

		return Department.builder()
			.id(mongo.getId())
			.parent(mongo.getParent())
			.name(mongo.getName())
			.build();
	}

	@Override
	public Department modify(String client, DepartmentModifyRequest request) {
		mongoTemplate.updateFirst(
			Query.query(
				Criteria.where(DepartmentMongo.Field._ID).is(request.getId())
					.and(DepartmentMongo.Field.Client).is(client)
			),
			Update.update(DepartmentMongo.Field.Parent, request.getParentId())
				.set(DepartmentMongo.Field.Name, request.getName()),
			DepartmentMongo.class
		);

		return Optional.ofNullable(mongoTemplate.findById(request.getId(), DepartmentMongo.class))
			.map(mongo -> Department.builder()
				.id(mongo.getId())
				.parent(mongo.getParent())
				.name(mongo.getName())
				.build())
			.orElse(null);
	}

	@Override
	public Department delete(String client, String id) {
		DepartmentMongo mongo = mongoTemplate.findAndRemove(
			Query.query(
				Criteria.where(DepartmentMongo.Field._ID).is(id)
					.and(DepartmentMongo.Field.Client).is(client)
			),
			DepartmentMongo.class
		);

		return Optional.ofNullable(mongo)
			.map(data -> Department.builder()
				.id(data.getId())
				.parent(data.getParent())
				.name(data.getName())
				.build())
			.orElse(null);
	}
}
