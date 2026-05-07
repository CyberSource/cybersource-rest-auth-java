package com.cybersource.authsdk.jwtsecurity.test;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.MACVerifier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cybersource.authsdk.jwtsecurity.JWTCryptoProcessorImpl;

public class JWTCryptoProcessorImplHmacTest {

	private JWTCryptoProcessorImpl cryptoProcessor;
	private byte[] validSecretKeyBytes;
	private String validKeyId;
	private String validContent;

	@Before
	public void setUp() {
		cryptoProcessor = new JWTCryptoProcessorImpl();
		// Base64-decode a known shared secret (same as test credentials)
		validSecretKeyBytes = Base64.getDecoder().decode("yBJxy6LjM2TmcPGu+GaJrHtkke25fPpUX+UY6/L/1tE=");
		validKeyId = "08c94330-f618-42a3-b09d-e1e43be5efda";
		validContent = "{\"iat\":1234567890,\"iss\":\"testrest\",\"jti\":\"test-uuid\"}";
	}

	@Test
	public void testSignWithHmacProducesValidJws() throws Exception {
		String result = cryptoProcessor.signWithHmac(validContent, validSecretKeyBytes, validKeyId, null);

		Assert.assertNotNull(result);
		String[] parts = result.split("\\.");
		Assert.assertEquals("JWS should have 3 parts", 3, parts.length);
	}

	@Test
	public void testSignWithHmacNullContent() throws Exception {
		String result = cryptoProcessor.signWithHmac(null, validSecretKeyBytes, validKeyId, null);
		Assert.assertNull(result);
	}

	@Test
	public void testSignWithHmacEmptyContent() throws Exception {
		String result = cryptoProcessor.signWithHmac("", validSecretKeyBytes, validKeyId, null);
		Assert.assertNull(result);
	}

	@Test
	public void testSignWithHmacNullSecretKey() throws Exception {
		String result = cryptoProcessor.signWithHmac(validContent, null, validKeyId, null);
		Assert.assertNull(result);
	}

	@Test
	public void testSignWithHmacEmptySecretKey() throws Exception {
		String result = cryptoProcessor.signWithHmac(validContent, new byte[0], validKeyId, null);
		Assert.assertNull(result);
	}

	@Test
	public void testSignWithHmacWithCustomHeaders() throws Exception {
		Map<String, Object> customHeaders = new HashMap<>();
		customHeaders.put("x-custom-header", "test-value");

		String result = cryptoProcessor.signWithHmac(validContent, validSecretKeyBytes, validKeyId, customHeaders);

		Assert.assertNotNull(result);
		// Parse header and verify custom header is present
		String headerJson = new String(Base64.getUrlDecoder().decode(result.split("\\.")[0]));
		Assert.assertTrue("Custom header should be in JWS header", headerJson.contains("x-custom-header"));
	}

	@Test
	public void testSignWithHmacWithoutCustomHeaders() throws Exception {
		// Should work with null customHeaders
		String result = cryptoProcessor.signWithHmac(validContent, validSecretKeyBytes, validKeyId, null);
		Assert.assertNotNull(result);

		// Also with empty map
		String result2 = cryptoProcessor.signWithHmac(validContent, validSecretKeyBytes, validKeyId, new HashMap<>());
		Assert.assertNotNull(result2);
	}

	@Test
	public void testSignWithHmacVerifiable() throws Exception {
		String result = cryptoProcessor.signWithHmac(validContent, validSecretKeyBytes, validKeyId, null);
		Assert.assertNotNull(result);

		// Verify the signature using the same secret key
		JWSObject jwsObject = JWSObject.parse(result);
		MACVerifier verifier = new MACVerifier(validSecretKeyBytes);
		Assert.assertTrue("HMAC signature should be verifiable with same key", jwsObject.verify(verifier));
	}
}
