package com.lijiajia3515.cairo.auth.modules.role;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class RoleSaveParam implements Serializable {
	private String id;

	private String name;

	private List<String> resources;
}
