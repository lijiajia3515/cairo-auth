package com.lijiajia3515.cairo.auth.service.modules.role;

import cn.hutool.core.util.IdUtil;
import com.lijiajia3515.cairo.auth.domain.mongo.Mongo;
import com.lijiajia3515.cairo.auth.domain.mongo.ResourceMongo;
import com.lijiajia3515.cairo.auth.domain.mongo.RoleMongo;
import com.lijiajia3515.cairo.auth.modules.resource.ResourceTreeNode;
import com.lijiajia3515.cairo.auth.modules.role.*;
import com.lijiajia3515.cairo.auth.service.modules.resource.ResourceConverter;
import com.lijiajia3515.cairo.core.exception.UnknownBusinessException;
import com.lijiajia3515.cairo.core.page.Page;
import com.lijiajia3515.cairo.security.SecurityConstants;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 服务 - 角色
 */
@Slf4j
@Service
public class RoleService {

	public final Collection<String> UNMODIFIABLE_ROLE_CODES = Collections.singletonList(SecurityConstants.Role.ADMIN);

	private final MongoTemplate mongoTemplate;

	public RoleService(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}


	/**
	 * 保存
	 */
	@Transactional(rollbackFor = Exception.class)
	RoleV2 save(String client, RoleSaveParam param) {
		validUnModifiableRole(Collections.singleton(param.getId()));
		RoleMongo role = RoleMongo.builder()
			.client(client)
			.code(Optional.ofNullable(param.getId()).orElse(IdUtil.objectId()))
			.name(param.getName())
			.resources(param.getResources())
			.build();
		RoleMongo insert = mongoTemplate.insert(role, Mongo.Collection.ROLE);
		log.debug("[role][insert] -> {}", insert);
		return findByCode(client, insert.getCode()).orElseThrow(() -> new UnknownBusinessException("code not found"));

	}

	/**
	 * 修改
	 *
	 * @param client client
	 * @param param  param
	 * @return role
	 */
	@Transactional(rollbackFor = Exception.class)
	Optional<RoleV2> modify(String client, @Validated RoleModifyParam param) {
		validUnModifiableRole(Collections.singleton(param.getId()));
		Query query = Query.query(Criteria.where(RoleMongo.FIELD.CLIENT).is(client).and(RoleMongo.FIELD.CODE).is(param.getId()));
		Update update = new Update();
		Optional.ofNullable(param.getName()).ifPresent(name -> update.set(RoleMongo.FIELD.NAME, name));
		Optional.ofNullable(param.getResources()).ifPresent(resourceIds -> update.set(RoleMongo.FIELD.RESOURCES, resourceIds));
		UpdateResult result = mongoTemplate.updateFirst(query, update, RoleMongo.class, Mongo.Collection.ROLE);
		log.debug("[role][modify] -> {}", result);
		return findByCode(client, param.getId());
	}

	/**
	 * 删除
	 *
	 * @param client client
	 * @param param  param
	 * @return delete role data
	 */
	@Transactional(rollbackFor = Exception.class)
	List<RoleV2> delete(@NotNull String client, @Validated RoleDeleteParam param) {
		validUnModifiableRole(param.getIds());
		Criteria criteria = Criteria.where(RoleMongo.FIELD.CLIENT).is(client);
		Optional.of(param.getIds()).filter(x -> !x.isEmpty()).ifPresent(x -> criteria.and(RoleMongo.FIELD.CODE).in(x));
		Query query = Query.query(criteria);

		List<RoleV2> deleteRoles = mongoTemplate.findAllAndRemove(query, RoleMongo.class, Mongo.Collection.ROLE)
			.stream()
			.map(x -> {
				List<ResourceTreeNode> resources = findResources(x);
				return RoleConverter.roleV2Optional(x, resources).orElseThrow(() -> new UnknownBusinessException("code not found"));
			})
			.collect(Collectors.toList());

		log.debug("[role][delete] -> {}", deleteRoles);
		return deleteRoles;

	}


	/**
	 * find page
	 *
	 * @param client client
	 * @param param  param
	 * @return role page
	 */
	List<Role> find(@NotNull String client, @Validated RoleFindParam param) {
		Criteria criteria = buildFindParamCriteria(client, param);
		Query query = Query.query(criteria);

		query.with(
			Sort.by(
				Sort.Order.asc(RoleMongo.FIELD.METADATA.SORT),
				Sort.Order.asc(RoleMongo.FIELD.METADATA.CREATED.AT),
				Sort.Order.asc(RoleMongo.FIELD.METADATA.CREATED.AT)
			)
		);

		return mongoTemplate.find(query, RoleMongo.class, Mongo.Collection.ROLE)
			.stream()
			.flatMap(x -> RoleConverter.roleOptional(x).stream())
			.collect(Collectors.toList());
	}


	/**
	 * find page
	 *
	 * @param client client
	 * @param param  param
	 * @return role page
	 */
	Page<Role> pageFind(@NotNull String client, @Validated RoleFindParam param) {
		Criteria criteria = buildFindParamCriteria(client, param);
		Query query = Query.query(criteria);

		long total = mongoTemplate.count(query, RoleMongo.class, Mongo.Collection.ROLE);

		query.with(param.pageable());
		List<Role> content = mongoTemplate.find(query, RoleMongo.class, Mongo.Collection.ROLE)
			.stream()
			.flatMap(x -> RoleConverter.roleOptional(x).stream())
			.collect(Collectors.toList());

		return new Page<>(param, content, total);
	}

	Criteria buildFindParamCriteria(@NotNull String client, @NotNull RoleFindParam param) {
		Criteria criteria = Criteria.where(RoleMongo.FIELD.CLIENT).is(client);
		Optional.ofNullable(param.getKeyword()).filter(kw -> !kw.isEmpty()).ifPresent(kw -> criteria.and(RoleMongo.FIELD.NAME).regex(kw));
		return criteria;
	}

	Optional<RoleV2> findByCode(String client, String code) {
		Query query = Query.query(Criteria.where(RoleMongo.FIELD.CLIENT).is(client).and(RoleMongo.FIELD.CODE).is(code));

		RoleMongo role = mongoTemplate.findOne(query, RoleMongo.class, Mongo.Collection.ROLE);

		List<ResourceTreeNode> resources = findResources(role);
		return RoleConverter.roleV2Optional(role, resources);
	}

	private List<ResourceTreeNode> findResources(RoleMongo role) {
		return Optional.ofNullable(role)
			.map(RoleMongo::getResources)
			.filter(x -> !x.isEmpty())
			.map(x ->
				mongoTemplate.find(
					Query.query(Criteria
						.where(RoleMongo.FIELD.CODE).is(role.getClient())
						.and(RoleMongo.FIELD._ID).in(x)),
					ResourceMongo.class,
					Mongo.Collection.RESOURCE)
			).stream().flatMap(Collection::stream)
			.map(ResourceConverter::resourceTreeNodeMapper)
			.collect(Collectors.toList());
	}

	private void validUnModifiableRole(Collection<String> codes) {
		List<String> unModifiableRoleCodes = codes.stream().filter(UNMODIFIABLE_ROLE_CODES::contains).collect(Collectors.toList());
		if (!unModifiableRoleCodes.isEmpty()) throw new AccessDeniedException(String.format("无权修改编辑 %s 角色", codes));
	}

}
