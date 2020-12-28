package com.hfhk.auth.domain.mongo;

public class Mongo {
	public static class Collection {
		private static final String PREFIX = "auth";

		public static final String CLIENT = collection("clients");


		public static final String USER = collection("users");

		public static final String ROLE = collection("roles");

		public static final String DEPARTMENT = collection("departments");

		public static final String RESOURCE = collection("resources");

		private static String collection(String collection) {
			return PREFIX.concat("_").concat(collection);
		}
	}
}
