package com.hfhk.cairo.auth.service.config;

import com.hfhk.cairo.core.security.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstant {

	@Bean("ROLE_ADMIN")
	public String roleAdmin() {
		return "ADMIN";
	}

	@Component("ROLE")
	public static class RoleRole {
		// role
		public String ROLE = "ROLE";

		// authority
		public String SAVE = "READ";
		public String MODIFY = "WRITE";
		public String DELETE = "DELETE";
	}

	@Component
	public static class SystemSecurityRole extends Role {

	}
}
