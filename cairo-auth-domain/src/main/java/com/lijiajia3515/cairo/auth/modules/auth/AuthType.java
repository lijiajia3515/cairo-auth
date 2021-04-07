package com.lijiajia3515.cairo.auth.modules.auth;

import java.io.Serializable;

public enum AuthType implements Serializable {
	Password,
	PhoneNumber,
	OAuth2,
	Wechat,
	Github,
}
