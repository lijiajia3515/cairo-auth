package com.hfhk.cairo.auth.domain.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
public class UserFindRequest implements Serializable {

	@Getter
	@Setter
	private String keyword;
}
