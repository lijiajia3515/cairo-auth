package com.hfhk.auth.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class RoleSaveRequest implements Serializable {
	private String code;

	private String name;

	private List<String> resources;
}
