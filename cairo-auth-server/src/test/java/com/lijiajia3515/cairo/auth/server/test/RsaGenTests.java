package com.lijiajia3515.cairo.auth.server.test;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import com.lijiajia3515.cairo.auth.server.AuthServerApp;
import com.lijiajia3515.cairo.auth.server.framework.security.oauth2.resourceserver.jwt.jose.Jwks;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


@SpringBootTest(classes = AuthServerApp.class)
@ExtendWith(SpringExtension.class)

public class RsaGenTests {
	@Test
	public void gen() throws JOSEException, NoSuchAlgorithmException, InvalidKeySpecException {
		RSAKey key = Jwks.generateRsa("2022");
		RSAPublicKey v1PublicKey = (RSAPublicKey) key.toPublicKey();
		RSAPrivateKey v1PrivateKey = (RSAPrivateKey) key.toPrivateKey();
		String privateKeyBase64Str = Base64Encoder.encode(v1PrivateKey.getEncoded());
		String publicKeyBase64Str = Base64Encoder.encode(v1PublicKey.getEncoded());

		System.out.println(privateKeyBase64Str);
		System.out.println(publicKeyBase64Str);

		X509EncodedKeySpec publicEncodedKeySpec = new X509EncodedKeySpec(Base64Decoder.decode(publicKeyBase64Str));
		PKCS8EncodedKeySpec privateEncodeKeySpec = new PKCS8EncodedKeySpec(Base64Decoder.decode(privateKeyBase64Str));

		KeyFactory factory = KeyFactory.getInstance("RSA");
		RSAPublicKey v2PublicKey = (RSAPublicKey) factory.generatePublic(publicEncodedKeySpec);
		RSAPrivateKey v2PrivateKey = (RSAPrivateKey) factory.generatePrivate(privateEncodeKeySpec);

		Assert.isTrue(v1PublicKey.equals(v2PublicKey), "公钥匹配正确");
		Assert.isTrue(v1PrivateKey.equals(v2PrivateKey), "私钥匹配正确");
	}
}
