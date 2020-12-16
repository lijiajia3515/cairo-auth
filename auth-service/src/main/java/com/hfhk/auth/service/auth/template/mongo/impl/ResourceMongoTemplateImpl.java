package com.hfhk.auth.service.auth.template.mongo.impl;

import com.hfhk.auth.service.auth.domain.mongo.ResourceMongo;
import com.hfhk.auth.service.auth.template.mongo.ResourceMongoTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * mongo template impl - resource
 */
@Slf4j
@Component
public class ResourceMongoTemplateImpl implements ResourceMongoTemplate {
	private final MongoTemplate mongoTemplate;

	public ResourceMongoTemplateImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<ResourceMongo> findSubsByIds(String client, Collection<String> ids) {
		Query query = Query.query(
			Criteria.where("client").is(client).and("_ids").in(ids)
		);
		List<ResourceMongo> firstList = mongoTemplate.find(query, ResourceMongo.class);
		List<ResourceMongo> allList = new ArrayList<>(firstList);
		findSubs(allList, firstList);

		return allList;
	}

	/**
	 * 根据父级查找 子集
	 *
	 * @param list list
	 */
	private void findSubs(List<ResourceMongo> data, List<ResourceMongo> list) {
		Optional.of(list.stream().map(ResourceMongo::getId).collect(Collectors.toSet()))
			.filter(x -> !x.isEmpty())
			.ifPresent(parentIds -> {
				List<ResourceMongo> subList = mongoTemplate.find(
					Query.query(
						Criteria.where("_id").in(parentIds)
					),
					ResourceMongo.class
				);
				data.addAll(subList);
				findSubs(data, subList);
			});

	}

}
