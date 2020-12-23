package com.hfhk.auth.service.modules.user;

import com.hfhk.auth.domain.mongo.UserMongo;
import com.hfhk.auth.domain.request.UserFindRequest;
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
				Criteria.where(UserMongo.Field.Uid)
					.regex(".*?\\" + request.getKeyword() + ".*")
					.orOperator(
						Criteria.where(UserMongo.Field.Username)
							.regex(".*?\\" + request.getKeyword() + ".*")
					)
					.orOperator(
						Criteria.where(UserMongo.Field.PhoneNumber)
							.regex(".*?\\" + request.getKeyword() + ".*")
					)
					.orOperator(
						Criteria.where(UserMongo.Field.Email)
							.regex(".*?\\" + request.getKeyword() + ".*")
					)
					.orOperator(
						Criteria.where(UserMongo.Field.Name)
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
