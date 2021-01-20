package com.hfhk.auth.modules.resource;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 资源-查询-参数
 */
@Data
public class ResourceFindParam implements Serializable {
	/**
	 * ids
	 */
	private List<String> ids;
}
