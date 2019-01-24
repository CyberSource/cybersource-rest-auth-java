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
import com.cybersource.authsdk.jwt.JwtSignatureTokenGenerator;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;

public class JWTSignatureTokenTest {

	@Mock
	JwtSignatureTokenGenerator jwtSignatureToken;
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
		merchantUtil.setRequestType(GlobalLabelParameters.GET);
		
		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails();

		jwtSignatureToken = new JwtSignatureTokenGenerator(merchantConfig,null, GlobalLabelParameters.GET);

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
		merchantUtil.setRequestType(GlobalLabelParameters.POST);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		
		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails();

		jwtSignatureToken = new JwtSignatureTokenGenerator(merchantConfig, merchantUtil.getRequestData(),GlobalLabelParameters.POST);

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
		merchantUtil.setRequestType(GlobalLabelParameters.PUT);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		
		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails();

		jwtSignatureToken = new JwtSignatureTokenGenerator(merchantConfig, merchantUtil.getRequestData(),GlobalLabelParameters.PUT);

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
		merchantUtil.setRequestType(GlobalLabelParameters.DELETE);
		
		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails();

		jwtSignatureToken = new JwtSignatureTokenGenerator(merchantConfig, null, GlobalLabelParameters.DELETE);

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
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(testJsonPath);
		merchantUtil.setRequestType(GlobalLabelParameters.PUT);
		
		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails();

		jwtSignatureToken = new JwtSignatureTokenGenerator(merchantConfig, merchantUtil.getRequestData(), GlobalLabelParameters.PUT);

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
		merchantUtil.setRequestType(GlobalLabelParameters.POST);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantConfig.setAuthenticationType("abc");
		logger = Log4j.getInstance(merchantConfig);
		
		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.AUTH_ERROR);
		
		MerchantUtil.validateMerchantDetails();
		jwtSignatureToken = new JwtSignatureTokenGenerator(merchantConfig, merchantUtil.getRequestData(), GlobalLabelParameters.POST);
        jwtSignatureToken.getToken();

	}

	@Test
	public void generateSignatureTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantUtil.setRequestType(GlobalLabelParameters.POST);
		
		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails();

		String token1 =null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token1);
		} else {
			jwtSignatureToken = new JwtSignatureTokenGenerator(merchantConfig, merchantUtil.getRequestData(), GlobalLabelParameters.POST);
			jwtSignatureToken.generateSignature();
			Cache cache=new Cache(merchantConfig);
			cache.lastModifiedTime = 1528;
			token1 = jwtSignatureToken.generateSignature();
			Assert.assertNotNull(token1);
		}

	}
}
