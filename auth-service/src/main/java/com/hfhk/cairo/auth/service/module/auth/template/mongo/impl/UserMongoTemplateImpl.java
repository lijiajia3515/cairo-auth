package com.hfhk.cairo.auth.service.module.auth.template.mongo.impl;

import com.hfhk.cairo.auth.domain.request.UserFindRequest;
import com.hfhk.cairo.auth.service.module.auth.domain.mongo.UserMongo;
import com.hfhk.cairo.auth.service.module.auth.template.mongo.UserMongoTemplate;
import com.hfhk.cairo.core.page.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class UserMongoTemplateImpl implements UserMongoTemplate {
	private final MongoTemplate mongoTemplate;

	public UserMongoTemplateImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Page<UserMongo> pageFind(UserFindRequest request, Pageable pageable) {
		Query query = new Query();
		Optional.ofNullable(request.getKeyword())
			.map(x ->
				Criteria.where("uid")
					.regex(".*?\\" + request.getKeyword() + ".*")
					.orOperator(
						Criteria.where("username")
							.regex(".*?\\" + request.getKeyword() + ".*")
					)
					.orOperator(
						Criteria.where("phoneNumber")
							.regex(".*?\\" + request.getKeyword() + ".*")
					)
					.orOperator(
						Criteria.where("email")
							.regex(".*?\\" + request.getKeyword() + ".*")
					)
					.orOperator(
						Criteria.where("name")
							.regex(".*?\\" + request.getKeyword() + ".*")
					)
			);
		long total = mongoTemplate.count(query, UserMongo.class);

		query.with(pageable);
		List<UserMongo> content = mongoTemplate.find(query, UserMongo.class);
		log.debug("[user][pageFind][query]: {}", query);
		return new Page<>(pageable, content, total);
	}
}
