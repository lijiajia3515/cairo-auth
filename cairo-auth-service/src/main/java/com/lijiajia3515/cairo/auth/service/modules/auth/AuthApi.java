package com.lijiajia3515.cairo.auth.service.modules.auth;

import com.lijiajia3515.cairo.security.authentication.RemoteUser;
import com.lijiajia3515.cairo.security.oauth2.server.resource.authentication.CairoAuthentication;
import com.lijiajia3515.cairo.security.oauth2.user.AuthPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j

@RestController
@RequestMapping("/auth")
public class AuthApi {

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public RemoteUser authenticationToken(@AuthenticationPrincipal AuthPrincipal principal) {
		return Optional.ofNullable(SecurityContextHolder.getContext())
			.map(SecurityContext::getAuthentication)
			.map(x -> (CairoAuthentication) x)
			.map(x -> RemoteUser.builder()
				.user(x.getPrincipal().getUser())
				.authorities(x.getAuthorities().parallelStream()
					.map(GrantedAuthority::getAuthority)
					.collect(Collectors.toSet())
				)
				.build()
			).orElseThrow();
	}
}
