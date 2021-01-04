package com.hfhk.auth.service.modules.index;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping
public class IndexApi {

	private final String applicationName;

	private final ServerProperties server;

	public IndexApi(@Value("${spring.application.name}") String applicationName, ServerProperties properties) {
		this.applicationName = applicationName;
		this.server = properties;
	}

	@RequestMapping()
	public String index() throws UnknownHostException {
		return String.format("hello %s %s:%s", applicationName, InetAddress.getLocalHost(), server.getPort());
	}
}
