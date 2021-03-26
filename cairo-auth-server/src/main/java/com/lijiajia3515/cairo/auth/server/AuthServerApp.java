package com.lijiajia3515.cairo.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lijiajia3515.cairo")
public class AuthServerApp {

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApp.class, args);
	}

}
