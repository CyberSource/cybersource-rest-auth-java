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
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;

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
	// private String requestType = "Http_Signature";
	private String getID = "5281957982016993203523";
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
		merchantConfig = new MerchantConfig(merchantProp);
		requestTarget = requestTarget + getID;
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setMerchantDetails();

		logger = Log4j.getInstance(merchantConfig);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setRequestType("GET");
		merchantConfig.setUrl(url);
		merchantUtil=new MerchantUtil(merchantConfig);
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
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setMerchantDetails();
		merchantConfig.setRequestTarget(requestTarget);
		
		logger = Log4j.getInstance(merchantConfig);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setRequestType("POST");
		merchantConfig.setUrl(url);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		
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
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setMerchantDetails();
		requestTarget = "/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest";
		merchantConfig.setRequestTarget(requestTarget);
		
		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setRequestType("PUT");
		merchantConfig.setUrl(url);
		merchantUtil=new MerchantUtil(merchantConfig);
		requestJsonPath = "src/test/resources/TRRReport.json";
		merchantUtil.setRequestJsonPath(requestJsonPath);
		
		logger = Log4j.getInstance(merchantConfig);
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
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setMerchantDetails();
		requestTarget = "/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest";
		merchantConfig.setRequestTarget(requestTarget);

		logger = Log4j.getInstance(merchantConfig);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setRequestType("DELETE");
		merchantConfig.setUrl(url);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath("not required");
		
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
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setMerchantDetails();
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantConfig.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);

		logger = Log4j.getInstance(merchantConfig);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setRequestType("POST");
		merchantConfig.setAuthenticationType("test");
		merchantConfig.setUrl(url);
		
		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.AUTH_ERROR );

		response=apiController.paymentPost(merchantConfig,null);
		

	}

	// test case for isMerchantConfigValid

	@Test
	public void PaymentTestIsMerchantConfigInValid() throws IOException, ConfigException, ClientException {
		Boolean check = false;
		try {
			merchantProp = PropertiesUtil.getMerchantProperties();
			merchantConfig = new MerchantConfig(merchantProp);
			merchantConfig.setMerchantDetails();
			merchantConfig.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);

			logger = Log4j.getInstance(merchantConfig);;

			url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
			merchantConfig.setRequestType("POST");
			merchantConfig.setUrl(url);
			merchantConfig.setRunEnvironment("test");
			merchantUtil=new MerchantUtil(merchantConfig);
			merchantUtil.setRequestJsonPath(requestJsonPath);
			apiController.paymentPost(merchantConfig,merchantUtil.getRequestData());
			thrown.expect(ConfigException.class);
		} catch (ConfigException e) {
			check = true;
		}  catch (ClientException e) {
			check = true;
		} catch (IOException e) {
			check = true;
		}
		
		if(check) {
			Assert.assertEquals(true, check);
		}
  }

	

	@Test
	public void commonApiControllerCheckTest() throws IOException, ConfigException, ClientException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);

		logger = Log4j.getInstance(merchantConfig);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setRequestType("GET");
		merchantConfig.setAuthenticationType("JWT");
		merchantConfig.setUrl(url);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		MerchantConfig.setSetMerchantDetails(false);

		apiController.commonApiControllerCheck(merchantConfig);
		Assert.assertNotNull(logger);
		MerchantConfig.setSetMerchantDetails(true);

	}
}
