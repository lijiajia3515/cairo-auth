package com.hfhk.auth.domain.mongo;

import com.hfhk.cairo.mongo.data.Metadata;
import com.hfhk.cairo.mongo.data.mapping.model.AbstractUpperCamelCaseField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;


/**
 * 角色
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleMongo implements Serializable {

	/**
	 * 标识
	 */
	private String id;

	/**
	 * 客户端id
	 */
	private String client;


	/**
	 * code编码
	 */
	private String code;

	/**
	 * 名称
	 */
	private String name;


	/**
	 * 资源id
	 */
	private List<String> resources;

	/**
	 * Metadata
	 */
	@Builder.Default
	private Metadata metadata = new Metadata();

	public static final Field FIELD = new Field();

	public static class Field extends AbstractUpperCamelCaseField {
		private Field(){

		}
		public final String CLIENT = field("Client");
		public final String CODE = field("Code");
		public final String NAME = field("Name");
		public final String RESOURCES = field("Resources");
	}
}
