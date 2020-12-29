package com.hfhk.auth.domain.role;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class RoleSaveParam implements Serializable {
	private String code;

	private String name;

	private List<String> resources;
}
