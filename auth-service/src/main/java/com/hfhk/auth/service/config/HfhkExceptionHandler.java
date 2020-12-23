package com.hfhk.auth.service.config;

import com.hfhk.cairo.core.business.DefaultBusiness;
import com.hfhk.cairo.core.exception.BusinessException;
import com.hfhk.cairo.core.result.BusinessResult;
import com.hfhk.cairo.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
@Configuration
public class HfhkExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.OK)
	public Result<Object> statusException(BusinessException e, HttpServletRequest request, HttpServletResponse response) {
		e.printStackTrace();
		log.info("[Exception] url-> [{}]", request.getRequestURI());
		HttpStatus status = (e.getStatus().success()) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		response.setStatus(status.value());
		return BusinessResult.build(e.getStatus(), e.getData());
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result<Object> runtimeException(RuntimeException e, HttpServletRequest request) {
		e.printStackTrace();
		log.info("[RuntimeException] url-> [{}]", request.getRequestURI());
		return BusinessResult.buildFailed();
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result<Object> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
		e.printStackTrace();
		log.info("[HttpRequestMethodNotSupportedException] url-> [{}]", request.getRequestURI());
		return new BusinessResult<>(false, DefaultBusiness.Failed.code(), e.getMessage(), e.getMethod());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public Result<Object> exception(Exception e, HttpServletRequest request) {
		e.printStackTrace();
		log.info("[Exception] url-> [{}]", request.getRequestURI());
		return BusinessResult.buildUnknown();
	}
}
