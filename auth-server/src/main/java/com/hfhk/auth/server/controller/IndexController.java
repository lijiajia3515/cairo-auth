package com.hfhk.auth.server.controller;

import com.hfhk.auth.server.domain.vo.IndexProverb;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class IndexController {

	@GetMapping
	public String index(Model model, Principal principal) {
		model.addAttribute("principal", principal);
		model.addAttribute("proverb", defaultIndexProverb());
		model.addAttribute("name", "湖北浩方恒科认证中心");
		return "index";
	}

	@GetMapping("/login")
	public String index(Model model) {
		model.addAttribute("proverb", defaultIndexProverb());
		model.addAttribute("name", "湖北浩方恒科认证中心");
		return "login";
	}

	public IndexProverb defaultIndexProverb() {
		return IndexProverb.builder()
			.ownerUserName("扎心龙")
			.ownerGroupName("湖北浩方恒科技术有限公司")
			.position("浩方推广负责人")
			.proverb("在互联网技术百花齐放的今天，代码管理工具却几乎被 Git 一统江湖。 Git 是分布式产品，具有速度快，扩展性好的特点，十分适合代码量大、用户多的企业。作为国内 Git 产品，码云对于国人来说，界面友好，响应客户需求及时，本地技术力量雄厚，是企业级应用的优秀选择。")
			.build();
	}
}
