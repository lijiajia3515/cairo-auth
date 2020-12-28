package com.hfhk.auth.service.modules.department;

import com.hfhk.auth.domain.department.DepartmentPageFindRequest;
import com.hfhk.auth.domain.mongo.DepartmentMongo;
import com.hfhk.auth.domain.mongo.Mongo;
import com.hfhk.cairo.core.page.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class DepartmentMongoTemplateImpl implements DepartmentMongoTemplate {

	private final MongoTemplate mongoTemplate;

	public DepartmentMongoTemplateImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}


	@Override
	public List<DepartmentMongo> findByIds(String client, Collection<String> ids) {
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
		return mongoTemplate.find(query, DepartmentMongo.class, Mongo.Collection.DEPARTMENT);
	}

	@Override
	public List<DepartmentMongo> find(String client) {
		Query query = Query
			.query(Criteria.where(DepartmentMongo.FIELD.CLIENT).is(client))
			.with(
				Sort.by(
					Sort.Order.asc(DepartmentMongo.FIELD.METADATA.SORT),
					Sort.Order.asc(DepartmentMongo.FIELD.METADATA.CREATED.AT),
					Sort.Order.asc(DepartmentMongo.FIELD._ID)
				)
			);

		return mongoTemplate.find(query, DepartmentMongo.class, Mongo.Collection.DEPARTMENT);
	}

	@Override
	public Page<DepartmentMongo> pageFind(String clientId, DepartmentPageFindRequest request) {
		Query query = Query
			.query(Criteria.where(DepartmentMongo.FIELD.CLIENT).is(clientId))
			.with(
				Sort.by(
					Sort.Order.asc(DepartmentMongo.FIELD.METADATA.SORT),
					Sort.Order.asc(DepartmentMongo.FIELD.METADATA.CREATED.AT),
					Sort.Order.asc(DepartmentMongo.FIELD._ID)
				)
			);
		Optional.ofNullable(request.getParent())
			.ifPresent(x -> query.addCriteria(Criteria.where(DepartmentMongo.FIELD.PARENT).is(request.getParent())));

		long total = mongoTemplate.count(query, DepartmentMongo.class, Mongo.Collection.DEPARTMENT);

		query.with(request.getPage().pageable());
		List<DepartmentMongo> content = mongoTemplate.find(query, DepartmentMongo.class, Mongo.Collection.DEPARTMENT);
		return new Page<>(request.getPage(), content, total);
	}
}
