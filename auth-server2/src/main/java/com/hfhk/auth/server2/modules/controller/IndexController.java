package com.hfhk.auth.server2.modules.controller;

import com.hfhk.auth.server2.modules.auth.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping
public class IndexController {
	@GetMapping
	public String index(@AuthenticationPrincipal AuthUser user,
						HttpServletRequest request,
						Model model) {

		model.addAttribute("user", user);
		return "index";
	}

	@GetMapping("/login")
	public String login(HttpSession session, Model model) {
		model.addAttribute("authenticationException", (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION));
		return "login";
	}

	@GetMapping("/logout")
	public String logout() {
		return "logout";
	}


}
