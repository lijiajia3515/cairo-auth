package com.hfhk.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthServerApp {

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApp.class, args);
	}

}
