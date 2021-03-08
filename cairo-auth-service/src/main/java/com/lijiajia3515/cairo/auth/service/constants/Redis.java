package com.lijiajia3515.cairo.auth.service.constants;

public class Redis {
	public static final String KEY_PREFIX = "Hfhk:Service:Auth:";

	public static class UserInfo {
		private static final String KEY = KEY_PREFIX.concat("UserInfo:%s:%s");

		public static String key(String client, String uid) {
			return String.format(KEY, client, uid);
		}
	}
}
