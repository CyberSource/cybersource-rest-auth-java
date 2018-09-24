package com.cybersource.authsdk.core.test;

import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.PropertiesUtil;

public class AuthorizationTest {

	@Mock
	Authorization authorization;
	
	@Mock
	MerchantConfig merchantConfig;
	@Mock
	Logger logger;

	private String requestJsonPath = "src/test/resources/request.json";
	private Properties merchantProp;

	private String getID = "5281957982016993203523";

	@Test
	public void getTokenTestForHttp() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setAuthenticationType("HTTP_SIGNATURE");

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(this.logger);

		authorization = new Authorization();
		String token = authorization.getToken(merchantConfig, merchantConfig.getMerchantKeyId(),
				merchantConfig.getMerchantSecretKey(), merchantConfig.getMerchantID(), merchantConfig.getRequestType(),
				getID);

		Assert.assertNotNull(token);
	}

	@Test
	public void getTokenTestForJwt() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.getRequestData();
		merchantConfig.setAuthenticationType("JWT");

		this.logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(this.logger);

		authorization = new Authorization();
		String token = authorization.getToken(merchantConfig, merchantConfig.getMerchantKeyId(),
				merchantConfig.getMerchantSecretKey(), merchantConfig.getMerchantID(), merchantConfig.getRequestType(),
				getID);

		Assert.assertNotNull(token);
	}

	@Test
	public void getTokenTestFail() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setAuthenticationType("JWE");

		this.logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(this.logger);
		authorization = new Authorization();
		String token = authorization.getToken(merchantConfig, merchantConfig.getMerchantKeyId(),
				merchantConfig.getMerchantSecretKey(), merchantConfig.getMerchantID(), merchantConfig.getRequestType(),
				getID);

		Assert.assertNull(token);
	}

	@Test
	public void setJWTRequestBodyTest() throws Exception {
		authorization = new Authorization();
		authorization.setJWTRequestBody(requestJsonPath);
		Assert.assertNotNull(requestJsonPath);
	}

	@Test
	public void setLoggerTest() throws Exception {
		authorization = new Authorization();
		authorization.setLogger(logger);
		Assert.assertNull(logger);
	}
}
