package com.hfhk.cairo.auth.service.module.auth.domain.mongo;

import com.hfhk.cairo.data.mongo.Metadata;
import com.hfhk.cairo.auth.service.module.auth.constants.Mongo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = Mongo.Collection.DEPARTMENT)

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentMongo {

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
	private String parentId;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 启用
	 */
	private Boolean enabled;

	/**
	 * 元信息
	 */
	@Builder.Default
	private Metadata metadata = new Metadata();
}
