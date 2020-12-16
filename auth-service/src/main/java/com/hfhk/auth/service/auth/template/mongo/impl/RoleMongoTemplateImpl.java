package com.hfhk.auth.service.auth.template.mongo.impl;

import com.hfhk.auth.service.auth.domain.mongo.RoleMongo;
import com.hfhk.auth.service.auth.template.mongo.RoleMongoTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class RoleMongoTemplateImpl implements RoleMongoTemplate {
	private final MongoTemplate mongoTemplate;

	public RoleMongoTemplateImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<RoleMongo> findByCodes(String clientId, Collection<String> roleCodes) {
		Query query = Query
			.query(
				Criteria.where("client").is(clientId)
					.and("code").in(roleCodes)
			).with(
				Sort.by(
					Sort.Order.asc("metadata.sort"),
					Sort.Order.asc("metadata.created.at"),
					Sort.Order.asc("_id")
				)
			);
		return mongoTemplate.find(query, RoleMongo.class);
	}
}
