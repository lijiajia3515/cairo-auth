package com.hfhk.auth.service.modules.authentication;

import com.hfhk.cairo.security.authentication.RemoteUser;
import com.hfhk.cairo.security.oauth2.server.resource.authentication.CairoAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;


@Slf4j

@RestController
@RequestMapping("/service/authentication")
public class AuthenticationService {

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public RemoteUser authenticationToken(@AuthenticationPrincipal CairoAuthenticationToken token) {
		// token.getClient().getId();
		log.debug("[api][authentication][token][client] : {}", token.getClient());
		log.debug("[api][authentication][token][user] : {}", token.getUser());
		return RemoteUser.builder()
			.user(token.getUser())
			.authorities(token.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
			.build();
	}
}
