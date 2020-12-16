package com.hfhk.auth.service.auth.service;

import com.hfhk.cairo.auth.domain.User;
import com.hfhk.cairo.auth.domain.request.UserFindRequest;
import com.hfhk.cairo.auth.domain.request.UserModifyRequest;
import com.hfhk.cairo.auth.domain.request.UserRegRequest;
import com.hfhk.cairo.auth.domain.request.UserResetPasswordRequest;
import com.hfhk.cairo.core.page.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * 用户 服务
 */
public interface UserService {

	/**
	 * 注册
	 *
	 * @param request 请求
	 */
	void reg(UserRegRequest request);

	/**
	 * 修改
	 *
	 * @param request request
	 * @return user
	 */
	Optional<User> modify(String client, UserModifyRequest request);

	/**
	 * 密码重置
	 *
	 * @param request request
	 * @return 重置后的密码
	 */
	String passwordReset(UserResetPasswordRequest request);

	/**
	 * find
	 *
	 * @return user page
	 */
	Page<User> find(String client, UserFindRequest request, Pageable pageable);

	/**
	 * find by id
	 *
	 * @param client client
	 * @param uid    uid
	 * @return user
	 */
	User findById(String client, String uid);
}
