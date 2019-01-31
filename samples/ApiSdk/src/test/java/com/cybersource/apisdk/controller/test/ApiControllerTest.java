package com.cybersource.apisdk.controller.test;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.apisdk.controller.ApiController;
import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;
import com.cybersource.authsdk.util.Utility;

public class ApiControllerTest {

	@Mock
	ApiController apiController;
	
	@Mock
	MerchantConfig merchantConfig;
	
	@Mock
	MerchantUtil merchantUtil;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Properties merchantProp;
	private String requestJsonPath = "src/test/resources/request.json";
	private String url = "https://";
	private String requestTarget = GlobalLabelParameters.REQUEST_TARGET;
	private Response response;
	Logger logger;

	@Before
	public void setUp() {
		apiController = new ApiController();
	}

	
	@Test
	public void PaymentTestForGet() throws IOException, ConfigException, ClientException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		String requestTarget = "/pts/v2/payments/5383909529456080803003";
		merchantUtil = new MerchantUtil(merchantConfig);
		merchantUtil.setRequestTarget(requestTarget);
		url = url + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
		merchantUtil.setUrl(url);
		merchantUtil.setRequestType("GET");
		merchantUtil.setRequestJsonPath("not required");
		
		if(MerchantUtil.validateMerchantDetails()) {
			response = apiController.paymentGet(merchantConfig,null);
		    Assert.assertNotNull(response.getResponseCode());
		} else {
		    thrown.expect(ConfigException.class);
			thrown.expectMessage(GlobalLabelParameters.MERCHANT_VALIDATION_ERROR );
		}

	}

	@Test
	public void PaymentTestForPost() throws IOException, ConfigException, ClientException  {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestTarget(requestTarget);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantUtil.setRequestType("POST");
		url = url + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
		merchantUtil.setUrl(url);
		
		if(MerchantUtil.validateMerchantDetails()) {
			response = apiController.paymentPost(merchantConfig, merchantUtil.getRequestData());
			Assert.assertNotNull(response.getResponseCode());
		} else {
			thrown.expect(ConfigException.class);
			thrown.expectMessage(GlobalLabelParameters.MERCHANT_VALIDATION_ERROR );
		}
	}
	
	@Test
	public void PaymentTestForPut() throws IOException, ConfigException, ClientException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		requestTarget = "/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest";
		
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestTarget(requestTarget);
		url = url + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
		merchantUtil.setUrl(url);
		requestJsonPath = "src/test/resources/TRRReport.json";
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantUtil.setRequestType("PUT");
		
		
		if(MerchantUtil.validateMerchantDetails()) {
			response = apiController.paymentPut(merchantConfig, merchantUtil.getRequestData());
			Assert.assertNotNull(response.getResponseCode());
		} else {
			thrown.expect(ConfigException.class);
			thrown.expectMessage(GlobalLabelParameters.MERCHANT_VALIDATION_ERROR );
		}	
		
	}
	
	@Test
	public void PaymentTestForDelete() throws IOException, ConfigException, ClientException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		requestTarget = "/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest";
		
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestTarget(requestTarget);
		url = url + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
		merchantUtil.setUrl(url);
		merchantUtil.setRequestJsonPath("not required");
		merchantUtil.setRequestType("DELETE");
		
		if(MerchantUtil.validateMerchantDetails()) {
			response = apiController.paymentDelete(merchantConfig, null);
			Assert.assertNotNull(response.getResponseCode());
		} else {
			thrown.expect(ConfigException.class);
			thrown.expectMessage(GlobalLabelParameters.MERCHANT_VALIDATION_ERROR );
		}
	}


	
	// test case for auth type

	@Test
	public void PaymentTestAuthPostFail() throws IOException, ConfigException, ClientException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		merchantConfig.setAuthenticationType("test");
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);

		url = url + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
		merchantUtil.setRequestType("POST");
		
		merchantUtil.setUrl(url);
		
		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.AUTH_ERROR );

		response=apiController.paymentPost(merchantConfig,null);
	}

	// test case for isMerchantConfigInValid

	@Test
	public void PaymentTestIsMerchantConfigInValid() throws IOException, ConfigException, ClientException {
			merchantProp = PropertiesUtil.getMerchantProperties();
			merchantConfig = MerchantConfig.getInstance(merchantProp);
			merchantConfig.setMerchantDetails();
			
			merchantConfig.setRunEnvironment("");
			merchantUtil=new MerchantUtil(merchantConfig);
			merchantUtil.setRequestJsonPath(requestJsonPath);
			merchantUtil.setRequestType("POST");
			merchantUtil.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);
			url = url + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
			merchantUtil.setUrl(url);
			
			thrown.expect(ConfigException.class);
			thrown.expectMessage(GlobalLabelParameters.RUN_ENVIRONMENT_ERR);
			
			apiController.paymentPost(merchantConfig,merchantUtil.getRequestData());
		
  }

	

	@Test
	public void commonApiControllerCheckTest() throws IOException, ConfigException, ClientException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);
		url = url + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
		merchantUtil.setUrl(url);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantUtil.setRequestType("GET");
		
		apiController.commonApiControllerCheck(merchantConfig);
		Assert.assertNotNull(Utility.checkAuthenticationValidation(merchantConfig.getAuthenticationType()));

	}
}
