package com.hfhk.auth.domain.mongo;

import com.hfhk.cairo.mongo.data.Metadata;
import com.hfhk.cairo.mongo.data.mapping.model.UpperCamelCaseFieldNames;
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

	public static final class Field extends UpperCamelCaseFieldNames {
		public static final String Client = "Client";
		public static final String Parent = "Parent";
		public static final String Name = "Name";
	}
}
