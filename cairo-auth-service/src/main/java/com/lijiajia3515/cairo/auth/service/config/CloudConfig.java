package com.lijiajia3515.cairo.auth.service.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
@EnableFeignClients(
	basePackages = "com.lijiajia3515.cairo.**.client"
)
public class CloudConfig {

}
