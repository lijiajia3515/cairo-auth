package com.hfhk.auth.service.modules.role;

import com.hfhk.auth.domain.mongo.Mongo;
import com.hfhk.auth.domain.mongo.RoleMongo;
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
	public List<RoleMongo> findByCodes(String client, Collection<String> roleCodes) {
		Query query = Query
			.query(
				Criteria.where(RoleMongo.FIELD.CLIENT).is(client)
					.and(RoleMongo.FIELD.CODE).in(roleCodes)
			).with(
				Sort.by(
					Sort.Order.asc(RoleMongo.FIELD.METADATA.SORT),
					Sort.Order.asc(RoleMongo.FIELD.METADATA.CREATED.AT),
					Sort.Order.asc(RoleMongo.FIELD._ID)
				)
			);
		return mongoTemplate.find(query, RoleMongo.class, Mongo.Collection.ROLE);
	}
}
