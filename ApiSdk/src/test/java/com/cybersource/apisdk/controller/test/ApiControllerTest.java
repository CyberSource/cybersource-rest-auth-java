package com.cybersource.apisdk.controller.test;

import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.apisdk.controller.ApiController;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

public class ApiControllerTest {

	@Mock
	ApiController apiController;
	@Mock
	MerchantConfig merchantConfig;

	private Properties merchantProp;
	// private String requestType = "Http_Signature";
	private String getID = "5281957982016993203523";
	private String requestJsonPath = "src/test/resources/request.json";
	private String url = "http://";
	private String requestTarget = GlobalLabelParameters.REQUEST_TARGET;
	Logger logger;

	@Before
	public void setUp() {
		apiController = new ApiController();
	}

	// test cases for http
	@Test
	public void PaymentTestForHttpGet() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		requestTarget = requestTarget + getID;
		merchantConfig.setRequestTarget(requestTarget);

		logger = Log4j.getInstance(merchantConfig);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setRequestType("GET");
		merchantConfig.setAuthenticationType("HTTP_Signature");
		merchantConfig.setURL(url);

		apiController.paymentGet(merchantConfig);
		Assert.assertEquals(200, Integer.parseInt(merchantConfig.getResponseCode()));

	}

	@Test
	public void PaymentTestForHttpPost() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestJsonPath(requestJsonPath);

		logger = Log4j.getInstance(merchantConfig);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setRequestType("POST");
		merchantConfig.setAuthenticationType("HTTP_Signature");
		merchantConfig.setURL(url);

		apiController.paymentPost(merchantConfig);
		Assert.assertEquals(201, Integer.parseInt(merchantConfig.getResponseCode()));

	}

	// test cases for JWT
	@Test
	public void PaymentTestForJwtGet() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		requestTarget = requestTarget + getID;
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestJsonPath(requestJsonPath);

		logger = Log4j.getInstance(merchantConfig);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setRequestType("GET");
		merchantConfig.setAuthenticationType("JWT");
		merchantConfig.setURL(url);

		apiController.paymentGet(merchantConfig);
		Assert.assertEquals(200, Integer.parseInt(merchantConfig.getResponseCode()));

	}

	@Test
	public void PaymentTestForJwtPost() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);
		merchantConfig.setRequestJsonPath(requestJsonPath);

		logger = Log4j.getInstance(merchantConfig);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setRequestType("POST");
		merchantConfig.setAuthenticationType("JWT");
		merchantConfig.setURL(url);

		apiController.paymentPost(merchantConfig);
		Assert.assertEquals(201, Integer.parseInt(merchantConfig.getResponseCode()));

	}

	// test case for auth type

	@Test
	public void PaymentTestAuthPostFail() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);

		logger = Log4j.getInstance(merchantConfig);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setRequestType("POST");
		merchantConfig.setAuthenticationType("test");
		merchantConfig.setURL(url);

		apiController.paymentPost(merchantConfig);
		Assert.assertEquals(null, merchantConfig.getResponseCode());

	}

	@Test
	public void PaymentTestAuthGetFail() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);

		logger = Log4j.getInstance(merchantConfig);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setRequestType("GET");
		merchantConfig.setAuthenticationType("test");
		merchantConfig.setURL(url);

		apiController.paymentGet(merchantConfig);
		Assert.assertEquals(true,MerchantConfig.isSetMerchantDetails);

	}

	// test case for isMerchantDetails

	@Test
	public void PaymentTestIsMerchantDetailsFailForPost() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);

		logger = Log4j.getInstance(merchantConfig);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setRequestType("POST");
		merchantConfig.setAuthenticationType("JWT");
		merchantConfig.setURL(url);

		apiController.paymentPost(merchantConfig);
		Assert.assertEquals(null, merchantConfig.getResponseCode());
	}

	@Test
	public void PaymentTestIsMerchantDetailsFailForGet() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);

		logger = Log4j.getInstance(merchantConfig);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setRequestType("GET");
		merchantConfig.setAuthenticationType("JWT");
		merchantConfig.setURL(url);
		merchantConfig.setKeyAlias("test");

		apiController.paymentGet(merchantConfig);
		Assert.assertEquals(null, merchantConfig.getResponseCode());

	}

	@Test
	public void commonApiControllerCheckTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);

		logger = Log4j.getInstance(merchantConfig);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setRequestType("GET");
		merchantConfig.setAuthenticationType("JWT");
		merchantConfig.setURL(url);
		MerchantConfig.isSetMerchantDetails = false;

		apiController.commonApiControllerCheck(merchantConfig);
		Assert.assertEquals(null, merchantConfig.getResponseCode());

	}
}
