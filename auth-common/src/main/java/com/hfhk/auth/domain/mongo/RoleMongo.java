package com.hfhk.auth.domain.mongo;

import com.hfhk.cairo.mongo.data.Metadata;
import com.hfhk.cairo.mongo.data.mapping.model.UpperCamelCaseFieldNames;
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

	public static class Field extends UpperCamelCaseFieldNames {
		public static final String Client = "Client";
		public static final String Code = "Code";
		public static final String Name = "Name";
		public static final String Resources = "Resources";
	}
}
