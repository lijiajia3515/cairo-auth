package com.lijiajia3515.auth.domain.mongo;

import com.lijiajia3515.cairo.mongo.data.Metadata;
import com.lijiajia3515.cairo.mongo.data.mapping.model.AbstractUpperCamelCaseField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 部门
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentMongo implements Serializable {

	/**
	 * 标识
	 */
	private String id;

	/**
	 * client
	 */
	private String client;

	/**
	 * 部门
	 */
	private String parent;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 元信息
	 */
	@Builder.Default
	private Metadata metadata = new Metadata();
	public static final Field FIELD = new Field();

	public static final class Field extends AbstractUpperCamelCaseField {
		private Field() {
		}

		public final String CLIENT = field("Client");
		public final String PARENT = field("Parent");
		public final String NAME = field("Name");
	}
}
