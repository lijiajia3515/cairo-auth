package com.lijiajia3515.cairo.auth.service.controller;

import com.lijiajia3515.cairo.auth.modules.auth.client.AuthenticationClient;
import com.lijiajia3515.cairo.security.authentication.RemoteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping
public class IndexController {

	private final String applicationName;

	private final ServerProperties server;

	public IndexController(@Value("${spring.application.name}") String applicationName, ServerProperties properties) {
		this.applicationName = applicationName;
		this.server = properties;
	}

	@RequestMapping()
	public String index() throws UnknownHostException {
		return String.format("hello %s %s:%s", applicationName, InetAddress.getLocalHost(), server.getPort());
	}

	@Autowired
	private AuthenticationClient client;

	@RequestMapping("/test")
	public RemoteUser test() {
		return client.authentication("1234");
	}
}
