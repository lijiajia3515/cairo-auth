package com.hfhk.auth.service.config;

import com.hfhk.cairo.starter.web.handler.ResultResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
	private final ResultResponseHandler resultResponseHandler;
	private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

	public WebConfig(ResultResponseHandler resultResponseHandler, MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
		this.resultResponseHandler = resultResponseHandler;
		this.mappingJackson2HttpMessageConverter = mappingJackson2HttpMessageConverter;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		log.info("Configured Spring Mvc Argument Resolvers");
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
		log.debug("Configured Spring Mvc Return Value Handlers");
		handlers.add(resultResponseHandler);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		log.info("Configured Spring Mvc Message Converters");
		converters.add(mappingJackson2HttpMessageConverter);
	}
}
