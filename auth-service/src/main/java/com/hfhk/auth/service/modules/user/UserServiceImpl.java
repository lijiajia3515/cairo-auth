package com.hfhk.auth.service.modules.user;

import cn.hutool.core.util.IdUtil;
import com.hfhk.auth.domain.department.Department;
import com.hfhk.auth.domain.mongo.DepartmentMongo;
import com.hfhk.auth.domain.mongo.RoleMongo;
import com.hfhk.auth.domain.mongo.UserMongo;
import com.hfhk.auth.domain.role.Role;
import com.hfhk.auth.domain.user.*;
import com.hfhk.auth.service.modules.department.DepartmentMongoTemplate;
import com.hfhk.auth.service.modules.role.RoleMongoTemplate;
import com.hfhk.cairo.core.page.Page;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务 实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
	private final MongoTemplate mongoTemplate;

	private final UserMongoRepository userMongoRepository;
	private final UserMongoTemplate userMongoTemplate;

	private final DepartmentMongoTemplate departmentMongoTemplate;
	private final RoleMongoTemplate roleMongoTemplate;

	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(
		MongoTemplate mongoTemplate,
		UserMongoRepository userMongoRepository,
		UserMongoTemplate userMongoTemplate,
		DepartmentMongoTemplate departmentMongoTemplate,
		RoleMongoTemplate roleMongoTemplate,
		PasswordEncoder passwordEncoder
	) {
		this.mongoTemplate = mongoTemplate;
		this.userMongoRepository = userMongoRepository;
		this.userMongoTemplate = userMongoTemplate;
		this.departmentMongoTemplate = departmentMongoTemplate;
		this.roleMongoTemplate = roleMongoTemplate;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void reg(UserRegRequest request) {
		UserMongo data = UserMongo.builder()
			.uid(UUID.randomUUID().toString())
			.name(Strings.isEmpty(request.getName()) ? request.getName() : request.getUsername())
			.username(request.getUsername())
			.phoneNumber(request.getPhoneNumber())
			.email(request.getEmail())
			.password(passwordEncoder.encode(Optional.ofNullable(request.getPassword()).orElse("123456")))
			.accountEnabled(true)
			.accountLocked(false)
			.clientDepartments(Collections.emptyMap())
			.clientRoles(Collections.emptyMap())
			.clientResources(Collections.emptyMap())
			.build();
		UserMongo insert = userMongoRepository.insert(data);
		log.debug("[user][reg] result -> {} ", insert);
	}

	@Override
	public Optional<User> modify(String client, UserModifyRequest request) {
		Query query = Query.query(Criteria.where("uid").is(request.getUid()));
		Update update = new Update();
		Optional.ofNullable(request.getUsername()).filter(x -> !x.isBlank()).ifPresent(x -> update.set("username", x));
		Optional.ofNullable(request.getPhoneNumber()).filter(x -> !x.isBlank()).ifPresent(x -> update.set("phoneNumber", x));
		Optional.ofNullable(request.getEmail()).filter(x -> !x.isBlank()).ifPresent(x -> update.set("email", x));
		Optional.ofNullable(request.getName()).filter(x -> !x.isBlank()).ifPresent(x -> update.set("name", x));

		Optional.ofNullable(request.getRoleCodes()).ifPresent(x -> update.set("clientRoles." + client, x));
		Optional.ofNullable(request.getDepartmentIds()).ifPresent(x -> update.set("clientDepartments." + client, x));
		Optional.ofNullable(request.getResourceIds()).ifPresent(x -> update.set("clientResources." + client, x));

		UpdateResult updateResult = mongoTemplate.updateFirst(query, update, UserMongo.class);
		log.debug("[user][update] result -> {}", updateResult);

		return findUserByUid(client, request.getUid());
	}

	@Override
	public String passwordReset(UserResetPasswordRequest request) {
		String password = Optional.ofNullable(request.getPassword()).orElse(IdUtil.objectId());
		Query query = Query.query(Criteria.where("uid").is(request.getUid()));
		Update update = Update.update("password", passwordEncoder.encode(password));

		UpdateResult updateResult = mongoTemplate.updateFirst(query, update, UserMongo.class);
		log.debug("[user][password_reset] result -> {}", updateResult);
		return password;
	}

	public Optional<User> findUserByUid(String clientId, String uid) {
		Query query = Query.query(Criteria.where("uid").is(uid));
		return Optional.ofNullable(mongoTemplate.findOne(query, UserMongo.class))
			.map(user -> {

				Set<String> roleCodes = Optional.ofNullable(user.getClientRoles()).stream()
					.filter(clientRole -> clientRole.containsKey(clientId))
					.flatMap(clientRole -> Optional.ofNullable(clientRole.get(clientId)).stream().flatMap(Collection::stream))
					.collect(Collectors.toSet());

				Set<String> departmentIds = Optional.ofNullable(user.getClientDepartments()).stream()
					.filter(clientDepartments -> clientDepartments.containsKey(clientId))
					.flatMap(clientDepartments -> Optional.ofNullable(clientDepartments.get(clientId)).stream().flatMap(Collection::stream))
					.collect(Collectors.toSet());

				List<RoleMongo> roles = roleMongoTemplate.findByCodes(clientId, roleCodes);
				List<DepartmentMongo> departments = departmentMongoTemplate.findByIds(clientId, departmentIds);

				return User.builder()
					.uid(user.getUid())
					.name(user.getName())
					.username(user.getUsername())
					.email(user.getEmail())
					.phoneNumber(user.getPhoneNumber())
					.avatarUrl(user.getAvatarUrl())
					.roles(roles.stream()
						.map(roleMongo ->
							Role.builder()
								.code(roleMongo.getCode())
								.name(roleMongo.getName())
								.build()
						)
						.collect(Collectors.toList())
					)
					.departments(
						departments.stream()
							.map(departmentMongo ->
								Department.builder()
									.id(departmentMongo.getId())
									.name(departmentMongo.getName())
									.build()
							)
							.collect(Collectors.toList())
					)
					.accountEnabled(user.getAccountEnabled())
					.accountLocked(user.getAccountLocked())
					.build();
			});
	}

	@Override
	public Page<User> find(String client,
						   UserPageFindRequest request) {
		Page<UserMongo> page = userMongoTemplate.pageFind(request);

		Set<String> roleCodes = page.stream().flatMap(x -> Optional.ofNullable(x.getClientRoles()).stream())
			.filter(clientRole -> clientRole.containsKey(client))
			.flatMap(clientRole -> Optional.ofNullable(clientRole.get(client)).stream().flatMap(Collection::stream))
			.collect(Collectors.toSet());

		Set<String> departmentIds = page.stream().flatMap(x -> Optional.ofNullable(x.getClientDepartments()).stream())
			.filter(clientDepartments -> clientDepartments.containsKey(client))
			.flatMap(clientDepartments -> Optional.ofNullable(clientDepartments.get(client)).stream().flatMap(Collection::stream))
			.collect(Collectors.toSet());
		List<RoleMongo> roles = roleMongoTemplate.findByCodes(client, roleCodes);
		List<DepartmentMongo> departments = departmentMongoTemplate.findByIds(client, departmentIds);

		List<User> users = page.stream()
			.map(u ->
				User.builder()
					.uid(u.getUid())
					.name(u.getName())
					.username(u.getUsername())
					.email(u.getEmail())
					.phoneNumber(u.getPhoneNumber())
					.avatarUrl(u.getAvatarUrl())
					.roles(
						Optional.ofNullable(u.getClientRoles())
							.flatMap(clientRoles -> Optional.ofNullable(clientRoles.get(client)))
							.map(userRoleCodes ->
								roles.stream()
									.filter(x -> userRoleCodes.contains(x.getCode()))
									.map(roleMongo ->
										Role.builder()
											.code(roleMongo.getCode())
											.name(roleMongo.getName())
											.build()
									)
									.collect(Collectors.toList()))
							.orElse(Collections.emptyList())
					)
					.departments(
						Optional.ofNullable(u.getClientDepartments())
							.flatMap(clientDepartments -> Optional.ofNullable(clientDepartments.get(client)))
							.map(userDepartmentIds ->
								departments.stream()
									.filter(x -> userDepartmentIds.contains(x.getId()))
									.map(departmentMongo ->
										Department.builder()
											.id(departmentMongo.getId())
											.name(departmentMongo.getName())
											.build()
									)
									.collect(Collectors.toList()))
							.orElse(Collections.emptyList())
					)
					.accountEnabled(u.getAccountEnabled())
					.accountLocked(u.getAccountLocked())
					.build()).collect(Collectors.toList());

		return new Page<>(request.getPage(), users, page.getTotal());
	}

	@Override
	public User findById(String client, String uid) {
		return Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where("uid").is(uid)), UserMongo.class))
			.map(x -> {
				List<Role> roles = Optional.ofNullable(x.getClientRoles()).filter(y -> y.containsKey(client))
					.map(y -> y.get(client))
					.map(codes ->
						mongoTemplate.find(Query.query(Criteria.where("code").in(codes)), RoleMongo.class)
							.stream()
							.map(role -> Role.builder().code(role.getCode()).name(role.getName()).build())
							.collect(Collectors.toList())
					)
					.orElse(Collections.emptyList());
				List<Department> departments = Optional.ofNullable(x.getClientDepartments()).filter(y -> y.containsKey(client))
					.map(y -> y.get(client))
					.map(ids ->
						mongoTemplate.find(Query.query(Criteria.where("_id").in(ids)), DepartmentMongo.class)
							.stream()
							.map(role -> Department.builder().id(role.getId()).name(role.getName()).build())
							.collect(Collectors.toList())
					)
					.orElse(Collections.emptyList());
				return User.builder()
					.uid(x.getUid())
					.username(x.getUsername())
					.phoneNumber(x.getPhoneNumber())
					.email(x.getEmail())
					.avatarUrl(x.getAvatarUrl())
					.roles(roles)
					.departments(departments)
					.accountEnabled(x.getAccountEnabled())
					.accountLocked(x.getAccountLocked())
					.build();
			})
			.orElse(null);
	}
}
