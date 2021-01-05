package com.hfhk.auth.service.constants;

public class Constant {
	public static final String TREE_ROOT = "0";

	public static class Resource {
		public static final String TREE_ROOT = Constant.TREE_ROOT;
	}

	public static class Role {
		/**
		 * ROOT
		 */
		public static final String TREE_ROOT = Constant.TREE_ROOT;

		/**
		 * 管理员
		 */
		public static final String ADMIN = "ROLE_ADMIN";

		/**
		 * 总站长
		 */
		public static final String STATION_MASTER = "ROLE_STATION_MASTER";

		/**
		 * 副站长
		 */
		public static final String STATION_DEPUTY = "ROLE_STATION_DEPUTY";

		/**
		 * 站长
		 */
		public static final String STATION_HEAD = "ROLE_STATION_HEAD";

		/**
		 * 综合管理岗-负责人
		 */
		public static final String MANAGEMENT_POST_MASTER = "ROLE_MANAGEMENT_POST_MASTER";

		/**
		 * 综合管理岗-工作人员
		 */
		public static final String MANAGEMENT_POST_STAFF = "ROLE_MANAGEMENT_POST_STAFF";

		/**
		 * 监督技术岗-负责人
		 */
		public static final String SUPERVISE_TECHNICAL_POST_MASTER = "ROLE_SUPERVISE_TECHNICAL_POST_MASTER";

		/**
		 * 监督技术岗-工作人员
		 */
		public static final String SUPERVISE_TECHNICAL_POST_STAFF = "ROLE_SUPERVISE_TECHNICAL_POST_STAFF";
	}
}
