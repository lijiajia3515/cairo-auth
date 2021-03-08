package com.lijiajia3515.cairo.auth.service.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;


@EnableDiscoveryClient
@EnableFeignClients(
	basePackages = {
		"com.hfhk.**.client"
	}
)

@Configuration
public class CloudConfig {
}
