package com.cybersource.authsdk.core.test;

import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;
import com.cybersource.authsdk.util.Utility;

public class RequestContextTest {

	@Mock
	RequestContext requestContext;
	
	@Mock
	MerchantConfig merchantConfig;
	
	private Properties merchantProp;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private String requestJsonPath = "src/test/resources/request.json";
	
	private String testJsonPath = "src/test/resources/test.json";

	@Test
	public void validateRequestMethodTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext=new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType("POST");
		Assert.assertEquals(true, Utility.isRequestMethodTypeValid(requestContext.getRequestMethodType()));
	}
	
	@Test
	public void getRequestBodyTestPass() throws ConfigException, IOException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestJsonPath(requestJsonPath);

		String requestData = requestContext.getRequestBody();
		Assert.assertNotNull(requestData);
	}

	@Test
	public void getRequestBodyTestFail() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType("POST");
		requestContext.setRequestJsonPath(testJsonPath);
		String Data = requestContext.getRequestBody();
		Assert.assertNotNull(Data);
	}
	
	@Test
	public void isRequestTypeMethodValidTestForGet() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType("GET");

		boolean check = Utility.isRequestMethodTypeValid(requestContext.getRequestMethodType());
		Assert.assertEquals(true, check);

	}

	@Test
	public void isRequestTypeMethodValidTestForPost() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType("POST");

		boolean check = Utility.isRequestMethodTypeValid(requestContext.getRequestMethodType());
		Assert.assertEquals(true, check);
	}

	@Test
	public void isRequestTypeMethodValidTestForPut() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType("PUT");

		boolean check = Utility.isRequestMethodTypeValid(requestContext.getRequestMethodType());
		Assert.assertEquals(true, check);
	}

	@Test
	public void isRequestTypeMethodValidTestForDelete() throws ConfigException, IOException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType("DELETE");

		boolean check = Utility.isRequestMethodTypeValid(requestContext.getRequestMethodType());
		Assert.assertEquals(true, check);
	}

	@Test
	public void isRequestTypeMethodValidTestFail() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType("option");

		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.REQUEST_VALIDATION_ERROR);
		Utility.isRequestMethodTypeValid(requestContext.getRequestMethodType());

	}

}
