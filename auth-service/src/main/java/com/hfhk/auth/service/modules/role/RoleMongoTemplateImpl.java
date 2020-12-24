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
				Criteria.where(RoleMongo.Field.Client).is(client)
					.and(RoleMongo.Field.Code).in(roleCodes)
			).with(
				Sort.by(
					Sort.Order.asc(RoleMongo.Field.Metadata.Sort),
					Sort.Order.asc(RoleMongo.Field.Metadata.Created.At),
					Sort.Order.asc(RoleMongo.Field._ID)
				)
			);
		return mongoTemplate.find(query, RoleMongo.class, Mongo.Collection.Role);
	}
}
