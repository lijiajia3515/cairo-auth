package com.lijiajia3515.cairo.auth.server.constants;

public class Redis {
	public static final String KEY_PREFIX = "auth-server:";

	public static class UserInfo {
		private static final String KEY = KEY_PREFIX.concat("userinfo:%s:%s");

		public static String key(String client, String uid) {
			return String.format(KEY, client, uid);
		}
	}
}
