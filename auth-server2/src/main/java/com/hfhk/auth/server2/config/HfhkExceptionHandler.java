package com.hfhk.auth.server2.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
@RestControllerAdvice
@Configuration
public class HfhkExceptionHandler {


	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void runtimeException(RuntimeException e, HttpServletRequest request) {
		log.info("[RuntimeException] url-> [{}]", request.getRequestURI());
		e.printStackTrace();
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public void httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
		log.info("[HttpRequestMethodNotSupportedException] url-> [{}]", request.getRequestURI());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public void exception(Exception e, HttpServletRequest request) {
		log.info("[Exception] url-> [{}]", request.getRequestURI());
		e.printStackTrace();
	}
}
