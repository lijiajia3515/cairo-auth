package com.hfhk.auth.service.modules.user;

import com.hfhk.auth.domain.mongo.Mongo;
import com.hfhk.auth.domain.mongo.UserMongo;
import com.hfhk.auth.domain.user.UserPageFindParam;
import com.hfhk.cairo.core.page.Page;
import lombok.extern.slf4j.Slf4j;
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
	public Page<UserMongo> pageFind(UserPageFindParam request) {
		Query query = new Query();
		Optional.ofNullable(request.getKeyword())
			.map(x ->
				Criteria.where(UserMongo.FIELD.UID)
					.regex(".*?\\" + request.getKeyword() + ".*")
					.orOperator(
						Criteria.where(UserMongo.FIELD.USERNAME)
							.regex(".*?\\" + request.getKeyword() + ".*")
					)
					.orOperator(
						Criteria.where(UserMongo.FIELD.PHONE_NUMBER)
							.regex(".*?\\" + request.getKeyword() + ".*")
					)
					.orOperator(
						Criteria.where(UserMongo.FIELD.EMAIL)
							.regex(".*?\\" + request.getKeyword() + ".*")
					)
					.orOperator(
						Criteria.where(UserMongo.FIELD.NAME)
							.regex(".*?\\" + request.getKeyword() + ".*")
					)
			);
		long total = mongoTemplate.count(query, UserMongo.class, Mongo.Collection.USER);

		query.with(request.getPage().pageable());
		List<UserMongo> content = mongoTemplate.find(query, UserMongo.class, Mongo.Collection.USER);
		log.debug("[user][pageFind][query]: {}", query);
		return new Page<>(request.getPage(), content, total);
	}
}
