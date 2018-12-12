package com.cybersource.authsdk.jwt.test;

import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.authsdk.cache.Cache;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.jwt.JwtSignatureToken;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;

public class JWTSignatureTokenTest {

	@Mock
	JwtSignatureToken jwtSignatureToken;
	@Mock
	MerchantConfig merchantConfig;
	@Mock
	MerchantUtil merchantUtil;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private String requestJsonPath = "src/test/resources/request.json";
	private String testJsonPath = "src/test/resources/test.json";
	private Properties merchantProp;

	@Mock
	Logger logger;

	@Test
	public void getTokenTestForGet() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantConfig.setRequestType("GET");
		
		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails(logger);

		jwtSignatureToken = new JwtSignatureToken(merchantConfig,null);

		String token =null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			token = jwtSignatureToken.getToken();
			Assert.assertNotNull(token);
		}

	}

	@Test
	public void getTokenTestForPost() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);		
		merchantUtil=new MerchantUtil(merchantConfig);
		
		merchantConfig.setRequestType("POST");
		merchantUtil.setRequestJsonPath(requestJsonPath);
		
		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails(logger);

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantUtil.getRequestData());

		String token = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			token = jwtSignatureToken.getToken();
			Assert.assertNotNull(token);
		}

	}

	@Test
	public void getTokenTestForPut() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);

		merchantConfig.setRequestType("PUT");
		merchantUtil.setRequestJsonPath(requestJsonPath);
		
		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails(logger);

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantUtil.getRequestData());

		String token = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			token = jwtSignatureToken.getToken();
			Assert.assertNotNull(token);
		}

	}

	@Test
	public void getTokenTestForDelete() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantConfig.setRequestType("DELETE");
		
		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails(logger);

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, null);

		String token = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			token = jwtSignatureToken.getToken();
			Assert.assertNotNull(token);
		}

	}

	@Test
	public void getTokenTestForEmptyRequestBody() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("PUT");

		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(testJsonPath);
		
		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails(logger);

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantUtil.getRequestData());

		String token = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			token = jwtSignatureToken.getToken();
			Assert.assertNotNull(token);
		}

	}

	@Test
	public void getTokenTestFail() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantConfig.setRequestType("POST");
		
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantConfig.setAuthenticationType("abc");
		logger = Log4j.getInstance(merchantConfig);
		
		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.AUTH_ERROR);
		
		MerchantUtil.validateMerchantDetails(logger);
		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantUtil.getRequestData());
        jwtSignatureToken.getToken();

	}

	@Test
	public void generateSignatureTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		
		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails(logger);

		String token1 =null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token1);
		} else {
			jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantUtil.getRequestData());
			jwtSignatureToken.generateSignature();
			Cache.lastModifiedTime = 1528;
			token1 = jwtSignatureToken.generateSignature();
			Assert.assertNotNull(token1);
		}

	}
}
