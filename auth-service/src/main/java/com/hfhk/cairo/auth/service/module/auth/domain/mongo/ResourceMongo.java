package com.hfhk.cairo.auth.service.module.auth.domain.mongo;

import com.hfhk.cairo.auth.domain.ResourceType;
import com.hfhk.cairo.auth.service.module.auth.constants.Mongo;
import com.hfhk.cairo.mongo.data.Metadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * mongo - 资源
 */
@Document(collection = Mongo.Collection.RESOURCES)

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceMongo {

	/**
	 * id
	 */
	private String id;

	/**
	 * client
	 */
	private String client;

	/**
	 * 上级id
	 */
	private String parentId;

	/**
	 * 类型
	 */
	private ResourceType type;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 接口所需要的权限标识
	 */
	private List<String> permissions;

	/**
	 * 前端路径/前端路由/页面地址外部地址
	 */
	private String path;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * Metadata
	 */
	@Builder.Default
	private Metadata metadata = new Metadata();

}
