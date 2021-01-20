package com.hfhk.auth.service.modules.user;

import com.hfhk.auth.domain.mongo.DepartmentMongo;
import com.hfhk.auth.domain.mongo.Mongo;
import com.hfhk.auth.domain.mongo.RoleMongo;
import com.hfhk.auth.domain.mongo.UserMongo;
import com.hfhk.auth.domain.user.*;
import com.hfhk.auth.modules.user.*;
import com.hfhk.cairo.core.Constants;
import com.hfhk.cairo.core.page.Page;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户 服务
 */
@Slf4j
@Service
public class UserService {
	private static final String DEFAULT_PASSWORD = "123456";

	private final MongoTemplate mongoTemplate;
	private final PasswordEncoder passwordEncoder;

	public UserService(
		MongoTemplate mongoTemplate,
		PasswordEncoder passwordEncoder) {
		this.mongoTemplate = mongoTemplate;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * 用户 注册
	 *
	 * @param param 请求
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public User reg(@Validated UserSaveParam param) {
		UserMongo data = UserMongo.builder()
			.uid(Constants.SNOWFLAKE.nextIdStr())
			.name(Strings.isEmpty(param.getName()) ? param.getName() : param.getUsername())
			.username(param.getUsername())
			.phoneNumber(param.getPhoneNumber())
			.email(param.getEmail())
			.password(passwordEncoder.encode(Optional.ofNullable(param.getPassword()).orElse(DEFAULT_PASSWORD)))
			.accountEnabled(false)
			.accountLocked(false)
			.clientDepartments(Collections.emptyMap())
			.clientRoles(Collections.emptyMap())
			.clientResources(Collections.emptyMap())
			.build();
		UserMongo insert = mongoTemplate.insert(data, Mongo.Collection.USER);
		log.debug("[user][reg] result -> {} ", insert);
		return findById(null, insert.getUid()).orElseThrow();
	}

	/**
	 * 后台 新增用户
	 *
	 * @param client client
	 * @param param  param
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public User save(@NotNull String client, @Validated UserSaveParam param) {
		UserMongo data = UserMongo.builder()
			.uid(Constants.SNOWFLAKE.nextIdStr())
			.name(Strings.isEmpty(param.getName()) ? param.getName() : param.getUsername())
			.username(param.getUsername())
			.phoneNumber(param.getPhoneNumber())
			.email(param.getEmail())
			.password(passwordEncoder.encode(Optional.ofNullable(param.getPassword()).orElse(DEFAULT_PASSWORD)))
			.accountEnabled(true)
			.accountLocked(false)
			.clientRoles(Collections.singletonMap(client, param.getRoleIds()))
			.clientDepartments(Collections.singletonMap(client, param.getDepartmentIds()))
			.clientResources(Collections.singletonMap(client, param.getResourceIds()))
			.build();
		UserMongo insert = mongoTemplate.insert(data, Mongo.Collection.USER);
		log.debug("[user][save] result -> {} ", insert);
		return findById(client, insert.getUid()).orElseThrow();
	}

	/**
	 * 修改
	 *
	 * @param request request
	 * @return user
	 */
	@Transactional(rollbackFor = Exception.class)
	public Optional<User> modify(@NotNull String client, @Validated UserModifyParam request) {
		Query query = Query.query(Criteria.where(UserMongo.FIELD.UID).is(request.getUid()));
		Update update = new Update();
		Optional.ofNullable(request.getUsername()).filter(x -> !x.isBlank()).ifPresent(x -> update.set(UserMongo.FIELD.USERNAME, x));
		Optional.ofNullable(request.getPhoneNumber()).filter(x -> !x.isBlank()).ifPresent(x -> update.set(UserMongo.FIELD.PHONE_NUMBER, x));
		Optional.ofNullable(request.getEmail()).filter(x -> !x.isBlank()).ifPresent(x -> update.set(UserMongo.FIELD.EMAIL, x));
		Optional.ofNullable(request.getName()).filter(x -> !x.isBlank()).ifPresent(x -> update.set(UserMongo.FIELD.NAME, x));

		Optional.ofNullable(request.getRoleIds()).ifPresent(x -> update.set(UserMongo.FIELD.CLIENT_ROLES.client(client), x));
		Optional.ofNullable(request.getDepartmentIds()).ifPresent(x -> update.set(UserMongo.FIELD.CLIENT_DEPARTMENTS.client(client), x));
		Optional.ofNullable(request.getResourceIds()).ifPresent(x -> update.set(UserMongo.FIELD.CLIENT_RESOURCES.client(client), x));

		UpdateResult updateResult = mongoTemplate.updateFirst(query, update, UserMongo.class, Mongo.Collection.USER);
		log.debug("[user][modify] result -> {}", updateResult);

		return findUserByUid(client, request.getUid());
	}


	/**
	 * 密码重置
	 *
	 * @param param param
	 * @return 重置后的密码
	 */
	@Transactional(rollbackFor = Exception.class)
	public Optional<String> resetPassword(@NotNull String client, @Validated UserResetPasswordParam param) {
		String password = Optional.ofNullable(param.getPassword()).orElse(DEFAULT_PASSWORD);
		Query query = Query.query(Criteria.where(UserMongo.FIELD.UID).is(param.getUid()));
		Update update = Update.update(UserMongo.FIELD.PASSWORD, passwordEncoder.encode(password));

		UserMongo updateUser = mongoTemplate.findAndModify(query, update, UserMongo.class, Mongo.Collection.USER);
		log.debug("[user][password reset] result -> {}", updateUser);
		return Optional.ofNullable(updateUser).map(x->password);
	}

	/**
	 * 密码重置
	 *
	 * @param param param
	 * @return 重置后的密码
	 */
	@Transactional(rollbackFor = Exception.class)
	public Optional<Boolean> modifyStatus(@NotNull String client, @Validated UserModifyStatusParam param) {
		Criteria criteria = Criteria.where(UserMongo.FIELD.UID).is(param.getUid());
		Query query = Query.query(criteria);
		Update update = Update.update(UserMongo.FIELD.ACCOUNT_ENABLED, param.getStatus());

		UserMongo updateUser = mongoTemplate.findAndModify(query, update, UserMongo.class, Mongo.Collection.USER);
		log.debug("[user][modify status] result -> {}", updateUser);

		return Optional.ofNullable(updateUser).map(UserMongo::getAccountEnabled);
	}


	public Optional<User> findUserByUid(@NotNull String client, @NotNull String uid) {
		Query query = Query.query(Criteria.where(UserMongo.FIELD.UID).is(uid));
		return Optional.ofNullable(mongoTemplate.findOne(query, UserMongo.class, Mongo.Collection.USER))
			.map(user -> {

				Set<String> roleCodes = Optional.ofNullable(user.getClientRoles()).stream()
					.filter(clientRole -> clientRole.containsKey(client))
					.flatMap(clientRole -> Optional.ofNullable(clientRole.get(client)).stream().flatMap(Collection::stream))
					.collect(Collectors.toSet());

				Set<String> departmentIds = Optional.ofNullable(user.getClientDepartments()).stream()
					.filter(clientDepartments -> clientDepartments.containsKey(client))
					.flatMap(clientDepartments -> Optional.ofNullable(clientDepartments.get(client)).stream().flatMap(Collection::stream))
					.collect(Collectors.toSet());

				List<RoleMongo> roles = findRoleByCodes(client, roleCodes);
				List<DepartmentMongo> departments = findDepartmentByIds(client, departmentIds);

				return UserConverter.userMapper(user, roles, departments);
			});
	}


	/**
	 * find
	 *
	 * @param client client
	 * @param param  param
	 * @return user list
	 */
	public List<User> find(@NotNull String client, @Validated UserFindParam param) {
		Criteria criteria = buildFindParamCriteria(client, param);
		Query query = new Query(criteria);

		List<UserMongo> users = mongoTemplate.find(query, UserMongo.class, Mongo.Collection.USER);
		log.debug("[user][find] query: {}", users);

		Collection<String> roleCodes = roleIdsMapper(client, users);
		Collection<String> departmentIds = departmentIdsMapper(client, users);

		List<RoleMongo> roles = findRoleByCodes(client, roleCodes);
		List<DepartmentMongo> departments = findDepartmentByIds(client, departmentIds);

		return UserConverter.usersMapper(client, users, roles, departments);
	}


	/**
	 * find page
	 *
	 * @return user page
	 */
	public Page<User> findPage(@NotNull String client, @Validated UserFindParam param) {
		Criteria criteria = buildFindParamCriteria(client, param);
		Query query = new Query(criteria);

		long total = mongoTemplate.count(query, UserMongo.class, Mongo.Collection.USER);

		query.with(param.pageable());
		List<UserMongo> users = mongoTemplate.find(query, UserMongo.class, Mongo.Collection.USER);
		log.debug("[user][pageFind] query: {}", users);

		Collection<String> roleCodes = roleIdsMapper(client, users);
		Collection<String> departmentIds = departmentIdsMapper(client, users);

		List<RoleMongo> roles = findRoleByCodes(client, roleCodes);
		List<DepartmentMongo> departments = findDepartmentByIds(client, departmentIds);

		List<User> contents = UserConverter.usersMapper(client, users, roles, departments);

		return new Page<>(param, contents, total);
	}

	/**
	 * find by id
	 *
	 * @param client client
	 * @param uid    uid
	 * @return user
	 */
	public Optional<User> findById(String client, String uid) {
		Criteria criteria = Criteria.where(UserMongo.FIELD.UID).is(uid);
		Query query = Query.query(criteria);

		return Optional.ofNullable(mongoTemplate.findOne(query, UserMongo.class, Mongo.Collection.USER))
			.map(user -> {
				// role query
				List<RoleMongo> roles = Optional.ofNullable(user.getClientRoles())
					.filter(clientRoles -> clientRoles.containsKey(client))
					.map(clientRoles -> clientRoles.get(client))
					.filter(x -> !x.isEmpty())
					.map(codes -> {
						Query roleQuery = Query.query(
							Criteria
								.where(RoleMongo.FIELD.CLIENT).is(client)
								.and(RoleMongo.FIELD.CODE).in(codes)
						);
						return mongoTemplate.find(roleQuery, RoleMongo.class, Mongo.Collection.ROLE);
					})
					.orElse(Collections.emptyList());

				// department query
				List<DepartmentMongo> departments = Optional.ofNullable(user.getClientDepartments())
					.filter(y -> y.containsKey(client))
					.map(y -> y.get(client))
					.filter(x -> !x.isEmpty())
					.map(ids -> {
						Query departmentQuery = Query.query(
							Criteria
								.where(DepartmentMongo.FIELD.CLIENT).is(client)
								.and(DepartmentMongo.FIELD._ID).in(ids)
						);
						return mongoTemplate.find(departmentQuery, DepartmentMongo.class, Mongo.Collection.DEPARTMENT);
					})
					.orElse(Collections.emptyList());
				return UserConverter.userMapper(user, roles, departments);
			});
	}

	public List<RoleMongo> findRoleByCodes(String client, Collection<String> roleCodes) {
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

	public List<DepartmentMongo> findDepartmentByIds(String client, Collection<String> ids) {
		return Optional.ofNullable(ids)
			.filter(Collection::isEmpty)
			.map(x -> {
				Query query = Query.query(
					Criteria.where(DepartmentMongo.FIELD.CLIENT).is(client)
						.and(DepartmentMongo.FIELD._ID).in(ids)
				);
				query.with(Sort.by(
					Sort.Order.asc(DepartmentMongo.FIELD.METADATA.SORT),
					Sort.Order.asc(DepartmentMongo.FIELD.METADATA.CREATED.AT),
					Sort.Order.asc(DepartmentMongo.FIELD._ID)
				));
				return mongoTemplate.find(query, DepartmentMongo.class, Mongo.Collection.DEPARTMENT);
			})
			.orElse(Collections.emptyList());
	}

	public Collection<String> roleIdsMapper(String client, List<UserMongo> users) {
		return users.stream().flatMap(x -> Optional.ofNullable(x.getClientRoles()).stream())
			.filter(clientRole -> clientRole.containsKey(client))
			.flatMap(clientRole -> Optional.ofNullable(clientRole.get(client)).stream().flatMap(Collection::stream))
			.collect(Collectors.toSet());
	}

	public Collection<String> departmentIdsMapper(String client, List<UserMongo> users) {
		return users.stream().flatMap(x -> Optional.ofNullable(x.getClientDepartments()).stream())
			.filter(clientDepartments -> clientDepartments.containsKey(client))
			.flatMap(clientDepartments -> Optional.ofNullable(clientDepartments.get(client)).stream().flatMap(Collection::stream))
			.collect(Collectors.toSet());
	}

	public Criteria buildFindParamCriteria(String client, UserFindParam param) {
		Criteria criteria = new Criteria();
		Optional.ofNullable(param.getKeyword()).flatMap(this::keywordCriteria).ifPresent(x -> x.andOperator(criteria));
		Optional.ofNullable(param.getUids()).flatMap(this::uidCriteria).ifPresent(x -> x.andOperator(criteria));
		Optional.ofNullable(param.getRoleIds()).flatMap(x -> roleCodeCriteria(client, x)).ifPresent(x -> x.andOperator(criteria));
		Optional.ofNullable(param.getStatuses()).flatMap(this::enabledCriteria).ifPresent(x -> x.andOperator(criteria));
		return criteria;
	}


	public Optional<Criteria> keywordCriteria(String keyword) {
		return Optional.ofNullable(keyword)
			.filter(x -> !x.isEmpty())
			.map(x -> new Criteria().orOperator(
				Criteria.where(UserMongo.FIELD.UID).regex(keyword),
				Criteria.where(UserMongo.FIELD.USERNAME).regex(keyword),
				Criteria.where(UserMongo.FIELD.PHONE_NUMBER).regex(keyword),
				Criteria.where(UserMongo.FIELD.EMAIL).regex(keyword),
				Criteria.where(UserMongo.FIELD.NAME).regex(keyword))
			);
	}

	public Optional<Criteria> uidCriteria(Collection<String> uids) {
		return Optional.ofNullable(uids)
			.filter(x -> !x.isEmpty())
			.map(x -> Criteria.where(UserMongo.FIELD.UID).in(x));
	}

	public Optional<Criteria> roleCodeCriteria(String client, Collection<String> roleCodes) {
		return Optional.ofNullable(roleCodes)
			.filter(x -> !x.isEmpty())
			.map(x -> Criteria.where(UserMongo.FIELD.CLIENT_ROLES.client(client)).in(roleCodes));
	}

	public Optional<Criteria> enabledCriteria(Collection<Boolean> statuses) {
		return Optional.ofNullable(statuses)
			.filter(x -> !x.isEmpty())
			.map(x -> x.stream().filter(Objects::nonNull).collect(Collectors.toList()))
			.map(x -> Criteria.where(UserMongo.FIELD.ACCOUNT_ENABLED).in(statuses));
	}


}
