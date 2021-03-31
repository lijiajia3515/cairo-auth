package com.lijiajia3515.cairo.auth.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

	/**
	 * 设置系统资源允许跨域访问
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		//设置允许跨域的路径
		registry.addMapping("/**")
			.allowedOriginPatterns("*")
			.allowCredentials(true)
			.allowedHeaders("*")
			.allowedMethods(HttpMethod.OPTIONS.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name(), HttpMethod.PUT.name(), HttpMethod.PATCH.name())
			//跨域允许时间
			.maxAge(Duration.ofDays(30).toSeconds());
	}
}
