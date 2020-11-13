package com.hfhk.cairo.auth.service.module.auth.template.mongo.impl;

import com.hfhk.cairo.auth.domain.request.DepartmentFindRequest;
import com.hfhk.cairo.auth.service.module.auth.domain.mongo.DepartmentMongo;
import com.hfhk.cairo.auth.service.module.auth.template.mongo.DepartmentMongoTemplate;
import com.hfhk.cairo.core.page.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
	public List<DepartmentMongo> findByIds(String clientId, Collection<String> ids) {
		Query query = Query
			.query(Criteria.where("client").is(clientId).and("_id").in(ids))
			.with(
				Sort.by(
					Sort.Order.asc("metadata.sort"),
					Sort.Order.asc("metadata.created.at"),
					Sort.Order.asc("_id")
				)
			);
		return mongoTemplate.find(query, DepartmentMongo.class);
	}

	@Override
	public List<DepartmentMongo> find(String client) {
		Query query = Query
			.query(Criteria.where("client").is(client))
			.with(
				Sort.by(
					Sort.Order.asc("metadata.sort"),
					Sort.Order.asc("metadata.created.at"),
					Sort.Order.asc("_id")
				)
			);

		return mongoTemplate.find(query, DepartmentMongo.class);
	}

	@Override
	public Page<DepartmentMongo> pageFind(String clientId, DepartmentFindRequest request, Pageable pageable) {
		Query query = Query
			.query(Criteria.where("client").is(clientId))
			.with(
				Sort.by(
					Sort.Order.asc("metadata.sort"),
					Sort.Order.asc("metadata.created.at"),
					Sort.Order.asc("_id")
				)
			);
		Optional.ofNullable(request.getParentId())
			.ifPresent(x -> query.addCriteria(Criteria.where("parentId").is(request.getParentId())));

		long total = mongoTemplate.count(query, DepartmentMongo.class);

		query.with(pageable);
		List<DepartmentMongo> content = mongoTemplate.find(query, DepartmentMongo.class);
		return new Page<>(pageable, content, total);
	}
}
