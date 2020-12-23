package com.hfhk.auth.domain.mongo;

public class Mongo {
	public static class Collection {
		private static final String PREFIX = "auth";

		public static final String Client = collection("clients");


		public static final String User = collection("users");

		public static final String Role = collection("roles");

		public static final String Department = collection("departments");

		public static final String Resource = collection("resources");

		private static String collection(String collection) {
			return PREFIX.concat("_").concat(collection);
		}
	}
}
