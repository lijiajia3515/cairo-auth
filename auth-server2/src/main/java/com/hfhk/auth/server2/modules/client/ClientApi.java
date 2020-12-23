package com.hfhk.auth.server2.modules.client;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientApi {
	private final ClientService clientService;

	public ClientApi(ClientService clientService) {
		this.clientService = clientService;
	}

	@PostMapping("/find")
	@PermitAll
	public List<RegisteredClient> find() {
		return this.clientService.all();
	}

	@PostMapping("/save")
	@PermitAll
	public RegisteredClient save(@RequestBody RegisteredClient client) {
		return this.clientService.save(client).orElseThrow();
	}

	@PostMapping("/modify")
	@PermitAll
	public RegisteredClient modify(@RequestBody RegisteredClient client) {
		return this.clientService.modifyByClientId(client).orElseThrow();
	}
}
