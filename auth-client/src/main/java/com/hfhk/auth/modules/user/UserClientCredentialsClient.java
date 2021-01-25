package com.hfhk.auth.modules.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FeignClient(name = "service-auth-v1", path = "/User", contextId = "service-auth-v1-user-clientCredential-client")
public interface UserClientCredentialsClient {

	/**
	 * find
	 *
	 * @param uids uids
	 * @return find map
	 */
	default Map<String, User> findMap(Collection<String> uids) {
		return find(UserFindParam.builder().uids(uids).build()).stream()
			.collect(Collectors.toMap(User::getUid, x -> x));
	}

	/**
	 * find
	 *
	 * @param param param
	 * @return user
	 */
	@PostMapping("/Find")
	List<User> find(@RequestBody UserFindParam param);

	/**
	 * find
	 *
	 * @param uid uid
	 * @return user
	 */
	@GetMapping(path = "/Find/{uid}")
	User findById(@PathVariable(name = "uid") String uid);


}
