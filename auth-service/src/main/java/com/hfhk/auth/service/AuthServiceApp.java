package com.hfhk.auth.service;

import com.hfhk.cairo.starter.service.autoconfigure.HfhkWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = HfhkWebConfiguration.class)
@EnableFeignClients
@EnableDiscoveryClient
public class AuthServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApp.class, args);
	}

}
