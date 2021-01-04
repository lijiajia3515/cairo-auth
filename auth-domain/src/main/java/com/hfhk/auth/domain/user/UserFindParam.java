package com.hfhk.auth.domain.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

@Data
public class UserFindParam implements Serializable {

	private String keyword;

	private Collection<String> uids;
}
