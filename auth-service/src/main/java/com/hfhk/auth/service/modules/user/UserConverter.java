package com.hfhk.auth.service.modules.user;

import com.hfhk.auth.domain.mongo.DepartmentMongo;
import com.hfhk.auth.domain.mongo.RoleMongo;
import com.hfhk.auth.domain.mongo.UserMongo;
import com.hfhk.auth.modules.user.User;
import com.hfhk.auth.service.modules.department.DepartmentConverter;
import com.hfhk.auth.service.modules.role.RoleConverter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserConverter {

	public static List<User> usersMapper(String client, List<UserMongo> users, List<RoleMongo> roles, List<DepartmentMongo> departments) {
		return users.stream()
			.map(user ->
				UserConverter.userMapper(
					user,
					Optional
						.ofNullable(user.getClientRoles())
						.flatMap(clientRoles -> Optional.ofNullable(clientRoles.get(client)))
						.map(userRoleCodes -> roles
							.stream()
							.filter(x -> userRoleCodes.contains(x.getCode()))
							.collect(Collectors.toList()))
						.orElse(Collections.emptyList()),
					Optional
						.ofNullable(user.getClientDepartments())
						.flatMap(clientDepartments -> Optional.ofNullable(clientDepartments.get(client)))
						.map(userDepartmentIds ->
							departments.stream()
								.filter(x -> userDepartmentIds.contains(x.getId()))
								.collect(Collectors.toList()))
						.orElse(Collections.emptyList())))
			.collect(Collectors.toList());
	}

	public static User userMapper(UserMongo user, List<RoleMongo> roles, List<DepartmentMongo> departments) {
		return User.builder()
			.uid(user.getUid())
			.name(user.getName())
			.username(user.getUsername())
			.email(user.getEmail())
			.phoneNumber(user.getPhoneNumber())
			.avatarUrl(user.getAvatarUrl())
			.roles(roles.stream().map(RoleConverter::roleMapper).collect(Collectors.toList()))
			.departments(departments.stream().map(DepartmentConverter::departmentMapper).collect(Collectors.toList()))
			.accountEnabled(user.getAccountEnabled())
			.accountLocked(user.getAccountLocked())
			.lastLoginAt(user.getLastLoginAt())
			.build();
	}
}
