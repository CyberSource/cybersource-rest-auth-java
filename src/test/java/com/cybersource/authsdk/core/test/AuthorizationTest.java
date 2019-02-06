package com.cybersource.authsdk.core.test;

import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

public class AuthorizationTest {


	@Mock
	MerchantConfig merchantConfig;
	
	@Mock
	RequestContext requestContext;
	
	private String requestJsonPath = "src/test/resources/request.json";
	private Properties merchantProp;

	@Test
	public void getTokenTestForHttp() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext=new RequestContext(merchantConfig.getLogger());
		merchantConfig.setMerchantDetails();
		requestContext.setRequestMethodType(GlobalLabelParameters.GET);
		requestContext.setRequestJsonPath(requestJsonPath);

		String token = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(token);
		} else {
			token = Authorization.getToken(merchantConfig,requestContext);
			Assert.assertNotNull(token);
		}
	}

	@Test
	public void getTokenTestForJwt() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext=new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.POST);
		merchantConfig.setMerchantDetails();
		requestContext.setRequestJsonPath(requestJsonPath);

		String token = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			token = Authorization.getToken(merchantConfig,requestContext);
			Assert.assertNotNull(token);
		}
	}

	@Test
	public void getTokenTestFail() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext=new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.POST);
		requestContext.setRequestJsonPath(requestJsonPath);
		merchantConfig.setAuthenticationType("JWE");

		String token = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)
				|| merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			token = Authorization.getToken(merchantConfig, requestContext);
		}
		Assert.assertNull(token);
	}
}
