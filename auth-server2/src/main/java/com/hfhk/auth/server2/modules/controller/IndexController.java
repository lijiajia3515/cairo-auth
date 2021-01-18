package com.hfhk.auth.server2.modules.controller;

import com.hfhk.auth.server2.domain.AuthUser;
import com.hfhk.cairo.security.oauth2.user.AuthPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping
public class IndexController {
	@GetMapping
	public String index(@AuthenticationPrincipal AuthUser user, Model model) {
		log.info("user: {}", user);
		model.addAttribute("user", user);
		model.addAttribute("aa", "aa");
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
}
