/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lijiajia3515.cairo.auth.server.jose;

import cn.hutool.core.util.IdUtil;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import org.springframework.util.Base64Utils;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public final class KeyUtils {
	private static KeyPairGenerator keyPairGenerator = null;
	private static List<List<String>> list = new ArrayList<>();

	private KeyUtils() {
	}

	static {
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		list.add(
			Arrays.asList(
				"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"
			)
		);
	}

	public static List<JWK> genStr() {
		return list.stream()
			.map(x -> {
				X509EncodedKeySpec privateSpec = new X509EncodedKeySpec(Base64Utils.decodeFromString(x.get(0)));
				X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(Base64Utils.decodeFromString(x.get(1)));
				try {
					RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(privateSpec);
					RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(publicSpec);
					return new RSAKey.Builder(publicKey)
						.privateKey(privateKey)
						.keyID(IdUtil.objectId())
						.build();
				} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
					e.printStackTrace();
					KeyPair keyPair = keyPairGenerator.generateKeyPair();
					return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
						.privateKey((RSAPrivateKey) keyPair.getPrivate())
						.keyID(IdUtil.randomUUID())
						.build();
				}
			})
			.collect(Collectors.toList());
	}
}
