package com.hfhk.auth.service.modules.test;

import cn.hutool.core.util.IdUtil;
import com.hfhk.cairo.core.exception.UnknownBusinessException;
import com.hfhk.cairo.starter.web.handler.BusinessResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/test")
public class TestApi {
	private final AtomicInteger i = new AtomicInteger();

	@GetMapping("/exception")
	@BusinessResult
	@PermitAll
	public Object exception() throws Exception {
		switch (i.getAndIncrement() % 4) {
			case 0:
				throw new UnknownBusinessException("鬼晓得什么异常");
			case 1:
				int i = 1 / 0;
				System.out.println(i);
			case 2:
				throw new RuntimeException("程序员跑到外太空了");
			default:
				throw new Exception("哦吼");
		}
	}

	@PostMapping("/exception2")
	@BusinessResult
	@PermitAll
	public Object exception2(@RequestBody Map<String, String> x) {
		return x;
	}

	@GetMapping("/auth")
	@BusinessResult
	@PreAuthorize("isAuthenticated()")
	public Authentication auth(Authentication authentication) {
		return authentication;
	}

	@GetMapping("/2")
	@BusinessResult
	@PermitAll
	public Object a(HttpServletRequest request) {
		return request.getSession(false).getAttribute(HttpSessionOAuth2AuthorizationRequestRepository.class
			.getName() + ".AUTHORIZATION_REQUEST");
	}

	@GetMapping("/session1")
	@BusinessResult
	@PermitAll
	public void sessionA(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession(false).setAttribute("A", IdUtil.objectId());
		System.out.println("1-" + request.getSession(false).getAttribute("A"));
		response.sendRedirect("http://localhost:7822/test/session2");
	}

	@GetMapping("/session2")
	@BusinessResult
	@PermitAll
	public void sessionB(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("2-" + request.getSession(false).getAttribute("A"));
		response.sendRedirect("http://localhost:7822/test/session1");
	}

	@GetMapping("/session")
	@BusinessResult
	@PermitAll
	public Object session(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		System.out.println("SESSION: " + session.getId());
		request.getSession().setAttribute("A", IdUtil.objectId());
		System.out.println("SESSION-A: " + request.getSession(false).getAttribute("A"));
		return request.getSession().getAttribute("A");
	}

	@GetMapping("/session21")
	@BusinessResult
	@PermitAll
	public void session21(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		System.out.println("SESSION: " + session.getId());
		System.out.println("SESSION-A: " + session.getAttribute("A"));
		response.sendRedirect("http://localhost:7823/test/session22");
	}

	@GetMapping("/session22")
	@BusinessResult
	@PermitAll
	public void session22(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		System.out.println("SESSION: " + session.getId());
		System.out.println("SESSION-A: " + session.getAttribute("A"));
		response.sendRedirect("http://127.0.0.1:7822/test/session21");
	}

}
