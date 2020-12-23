package com.hfhk.auth.service.constants;

public class Redis {
	public static final String KEY_PREFIX = "HARPOON:SERVICE:AUTH:";

	public static class Token {
		private static final String KEY = KEY_PREFIX.concat("TOKEN:%s:%s");

		public static String key(String client, String uid) {
			return String.format(KEY, client, uid);
		}
	}
}
