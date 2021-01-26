package com.hfhk.auth.service.modules.department;

import com.hfhk.auth.domain.mongo.DepartmentMongo;
import com.hfhk.auth.domain.mongo.Mongo;
import com.hfhk.auth.modules.department.*;
import com.hfhk.cairo.core.CoreConstants;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.core.tree.TreeConverter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 服务 - 部门
 */
@Service
public class DepartmentService {

	private final MongoTemplate mongoTemplate;

	public DepartmentService(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	/**
	 * 部门 - 保存
	 *
	 * @param client client
	 * @param param  param
	 * @return 已保存的部门
	 */
	Optional<Department> save(String client, DepartmentSaveParam param) {
		DepartmentMongo mongo = DepartmentMongo.builder()
			.client(client)
			.parent(param.getParentId())
			.name(param.getName())
			.build();
		mongo.getMetadata().setSort(CoreConstants.SNOWFLAKE.nextId());

		mongo = mongoTemplate.insert(mongo);

		return DepartmentConverter.departmentOptional(mongo);
	}

	/**
	 * 部门 - 休息
	 *
	 * @param client client
	 * @param param  param
	 * @return 已修改的部门
	 */
	@Transactional(rollbackFor = Exception.class)
	public Optional<Department> modify(String client, DepartmentModifyParam param) {
		mongoTemplate.updateFirst(
			Query.query(
				Criteria.where(DepartmentMongo.FIELD._ID).is(param.getId())
					.and(DepartmentMongo.FIELD.CLIENT).is(client)
			),
			Update.update(DepartmentMongo.FIELD.PARENT, param.getParentId())
				.set(DepartmentMongo.FIELD.NAME, param.getName()),
			DepartmentMongo.class
		);
		return findByIds(client, Collections.singleton(param.getId())).findFirst();
	}

	/**
	 * 部门删除
	 *
	 * @param client client
	 * @param param  部门
	 * @return 已删除 部门
	 */
	List<Department> delete(String client, DepartmentDeleteParam param) {
		List<DepartmentMongo> content = mongoTemplate.findAllAndRemove(
			Query.query(
				Criteria.where(DepartmentMongo.FIELD._ID).is(param)
					.and(DepartmentMongo.FIELD.CLIENT).is(client)
			),
			DepartmentMongo.class
		);
		return content.stream().map(DepartmentConverter::departmentMapper).collect(Collectors.toList());
	}

	public List<Department> find(@NotNull String client, @Validated DepartmentFindParam param) {
		Criteria criteria = buildFindParam(client, param);
		Query query = Query
			.query(criteria)
			.with(
				Sort.by(
					Sort.Order.asc(DepartmentMongo.FIELD.METADATA.SORT),
					Sort.Order.asc(DepartmentMongo.FIELD.METADATA.CREATED.AT),
					Sort.Order.asc(DepartmentMongo.FIELD._ID)
				)
			);

		return mongoTemplate.find(query, DepartmentMongo.class, Mongo.Collection.DEPARTMENT)
			.stream()
			.map(DepartmentConverter::departmentMapper)
			.collect(Collectors.toList());

	}


	/**
	 * 查找
	 *
	 * @param client client
	 * @return 部门查询
	 */
	public Page<Department> findPage(@NotNull String client, @Validated DepartmentFindParam param) {
		Criteria criteria = buildFindParam(client, param);
		Query query = Query
			.query(criteria)
			.with(
				Sort.by(
					Sort.Order.asc(DepartmentMongo.FIELD.METADATA.SORT),
					Sort.Order.asc(DepartmentMongo.FIELD.METADATA.CREATED.AT),
					Sort.Order.asc(DepartmentMongo.FIELD._ID)
				)
			);

		long total = mongoTemplate.count(query, DepartmentMongo.class, Mongo.Collection.DEPARTMENT);

		query.with(param.pageable());
		query.with(
			Sort.by(
				Sort.Order.asc(DepartmentMongo.FIELD.METADATA.SORT),
				Sort.Order.asc(DepartmentMongo.FIELD.METADATA.CREATED.AT),
				Sort.Order.asc(DepartmentMongo.FIELD._ID)
			)
		);
		List<Department> contents = mongoTemplate.find(query, DepartmentMongo.class, Mongo.Collection.DEPARTMENT)
			.stream()
			.map(DepartmentConverter::departmentMapper)
			.collect(Collectors.toList());

		return new Page<>(param, contents, total);
	}

	/**
	 * 属性 查询
	 *
	 * @param client client
	 * @return 部门 list
	 */
	public List<DepartmentTreeNode> treeFind(String client) {
		List<DepartmentTreeNode> nodes = mongoTemplate.find(
			Query.query(Criteria.where(DepartmentMongo.FIELD.CLIENT).is(client)),
			DepartmentMongo.class,
			Mongo.Collection.DEPARTMENT
		).stream()
			.map(DepartmentConverter::departmentTreeNodeMapper)
			.collect(Collectors.toList());

		return TreeConverter.build(nodes, Constant.DEPARTMENT_TREE_ROOT, Constant.TREE_COMPARATOR);
	}


	Criteria buildFindParam(@NotNull String client, @NotNull DepartmentFindParam param) {
		Criteria criteria = Criteria.where(DepartmentMongo.FIELD.CLIENT).is(client);
		Optional.ofNullable(param.getParent()).ifPresent(parent -> criteria.and(DepartmentMongo.FIELD.PARENT).is(parent));
		return criteria;
	}


	private Stream<Department> findByIds(String client, Collection<String> ids) {
		Query query = Query
			.query(
				Criteria.where(DepartmentMongo.FIELD.CLIENT).is(client)
					.and(DepartmentMongo.FIELD.NAME).in(ids))
			.with(
				Sort.by(
					Sort.Order.asc(DepartmentMongo.FIELD.METADATA.SORT),
					Sort.Order.asc(DepartmentMongo.FIELD.METADATA.CREATED.AT),
					Sort.Order.asc(DepartmentMongo.FIELD._ID)
				)
			);
		return mongoTemplate.find(query, DepartmentMongo.class, Mongo.Collection.DEPARTMENT)
			.stream().map(DepartmentConverter::departmentMapper);
	}


}
