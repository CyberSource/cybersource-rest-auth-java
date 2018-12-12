package com.cybersource.authsdk.core.test;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;

public class AuthorizationTest {

	@Mock
	Authorization authorization;

	@Mock
	MerchantConfig merchantConfig;
	
	@Mock
	MerchantUtil merchantUtil;
	
	@Mock
	Logger logger;

	private String requestJsonPath = "src/test/resources/request.json";
	private Properties merchantProp;

	@Test
	public void getTokenTestForHttp() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantConfig.setRequestType("GET");
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantConfig.setAuthenticationType("HTTP_SIGNATURE");

		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails(this.logger);

		authorization = new Authorization();
		String token = authorization.getToken(merchantConfig,merchantUtil.getRequestData());
		Assert.assertNotNull(token);
	}

	@Test
	public void getTokenTestForJwt() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantConfig.setRequestType("POST");
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantUtil.getRequestData();
		merchantConfig.setMerchantDetails();

		this.logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails(this.logger);

		authorization = new Authorization();
		String token = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			token = authorization.getToken(merchantConfig,merchantUtil.getRequestData());
			Assert.assertNotNull(token);
		}
	}

	@Test
	public void getTokenTestFail() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantConfig.setRequestType("POST");
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantConfig.setAuthenticationType("JWE");

		this.logger = Log4j.getInstance(merchantConfig);
		authorization = new Authorization();
		String token = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)
				|| merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			MerchantUtil.validateMerchantDetails(this.logger);
			token = authorization.getToken(merchantConfig,merchantUtil.getRequestData());
		}
		Assert.assertNull(token);
	}
}
