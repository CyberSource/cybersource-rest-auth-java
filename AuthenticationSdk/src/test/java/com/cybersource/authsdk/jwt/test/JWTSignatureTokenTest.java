package com.cybersource.authsdk.jwt.test;

import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.authsdk.cache.Cache;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.jwt.JwtSignatureToken;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.PropertiesUtil;

public class JWTSignatureTokenTest {

	@Mock
	JwtSignatureToken jwtSignatureToken;
	@Mock
	MerchantConfig merchantConfig;

	private String requestJsonPath = "src/test/resources/request.json";
	private String testJsonPath = "src/test/resources/test.json";
	private Properties merchantProp;

	@Mock
	Logger logger;

	@Test
	public void getTokenTestForGet() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		merchantConfig.setAuthenticationType("JWT");

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantConfig.getRequestData(), logger,
				merchantConfig.getRequestType());

		String token = jwtSignatureToken.getToken();
		Assert.assertNotNull(token);

	}

	@Test
	public void getTokenTestForPost() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		;
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setAuthenticationType("JWT");

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantConfig.getRequestData(), logger,
				merchantConfig.getRequestType());

		String token = jwtSignatureToken.getToken();
		Assert.assertNotNull(token);

	}

	@Test
	public void getTokenTestForPut() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("PUT");
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setAuthenticationType("JWT");

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantConfig.getRequestData(), logger,
				merchantConfig.getRequestType());

		String token = jwtSignatureToken.getToken();
		Assert.assertNotNull(token);

	}

	@Test
	public void getTokenTestForDelete() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("DELETE");
		merchantConfig.setAuthenticationType("JWT");

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantConfig.getRequestData(), logger,
				merchantConfig.getRequestType());

		String token = jwtSignatureToken.getToken();
		Assert.assertNotNull(token);

	}

	@Test
	public void getTokenTestForEmptyRequestBody() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("PUT");
		merchantConfig.setRequestJsonPath(testJsonPath);
		merchantConfig.setAuthenticationType("JWT");

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantConfig.getRequestData(), logger,
				merchantConfig.getRequestType());

		String token = jwtSignatureToken.getToken();
		Assert.assertNotNull(token);

	}

	@Test
	public void getTokenTestForJwe() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setAuthenticationType("jwe");

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantConfig.getRequestData(), logger,
				merchantConfig.getRequestType());

		String token = jwtSignatureToken.getToken();
		Assert.assertNull(token);

	}

	@Test
	public void getTokenTestFail() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setAuthenticationType("abc");

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantConfig.getRequestData(), logger,
				merchantConfig.getRequestType());

		String token = jwtSignatureToken.getToken();
		Assert.assertNull(token);

	}

	@Test
	public void generateSignatureTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setAuthenticationType("jwt");

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantConfig.getRequestData(), logger,
				merchantConfig.getRequestType());
		jwtSignatureToken.generateSignature();
		Cache.lastModifiedTime = 1528;
		String token1 = jwtSignatureToken.generateSignature();
		Assert.assertNotNull(token1);

	}
}
