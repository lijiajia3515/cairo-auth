package com.hfhk.cairo.auth.service.module.auth.domain.mongo;

import com.hfhk.cairo.auth.service.module.auth.constants.Mongo;
import com.hfhk.cairo.mongo.data.Metadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = Mongo.Collection.ROLE)

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleMongo {

	/**
	 * 标识
	 */
	private String id;


	// private String parentId;

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
	 * 启用
	 */
	private Boolean enabled;

	/**
	 * Metadata
	 */
	@Builder.Default
	private Metadata metadata = new Metadata();

}
