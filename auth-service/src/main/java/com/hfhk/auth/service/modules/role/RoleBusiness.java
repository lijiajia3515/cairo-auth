package com.hfhk.auth.service.modules.role;

import com.hfhk.cairo.core.business.Business;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public enum RoleBusiness implements Business {
	/**
	 * 业务默认成功结果
	 */
	Success(true, "Success"),

	/**
	 * 业务默认失败结果
	 */
	Failed("操作失败"),

	/**
	 * 未知异常
	 */
	Unknown("未知异常,请联系管理员");

	public final boolean success;
	public final String code = name();
	public final String message;


	RoleBusiness(String message) {
		this.success = false;
		this.message = message;
	}

	RoleBusiness(boolean success, String message) {
		this.success = success;
		this.message = message;
	}
}
