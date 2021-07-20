package com.cybersource.apisdk.controller.test;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.apisdk.controller.ApiController;
import com.cybersource.apisdk.model.Response;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.test.resources.UtilityHelpers;

public class ApiControllerTest {

	@Mock
	ApiController apiController;
	@Mock
	MerchantConfig merchantConfig;

	private Properties merchantProp;
	private String getID = "5281957982016993203523";
	private String requestJsonData = "{\r\n"
			+ "  \"clientReferenceInformation\": {\r\n"
			+ "    \"code\": \"TC50171_3\"\r\n"
			+ "  },\r\n"
			+ "  \"processingInformation\": {\r\n"
			+ "    \"commerceIndicator\": \"internet\"\r\n"
			+ "  },\r\n"
			+ "  \"aggregatorInformation\": {\r\n"
			+ "    \"subMerchant\": {\r\n"
			+ "      \"cardAcceptorID\": \"1234567890\",\r\n"
			+ "      \"country\": \"US\",\r\n"
			+ "      \"phoneNumber\": \"650-432-0000\",\r\n"
			+ "      \"address1\": \"900 Metro Center\",\r\n"
			+ "      \"postalCode\": \"94404-2775\",\r\n"
			+ "      \"locality\": \"Foster City\",\r\n"
			+ "      \"name\": \"Visa Inc\",\r\n"
			+ "      \"administrativeArea\": \"CA\",\r\n"
			+ "      \"region\": \"PEN\",\r\n"
			+ "      \"email\": \"test@cybs.com\"\r\n"
			+ "    },\r\n"
			+ "    \"name\": \"V-Internatio\",\r\n"
			+ "    \"aggregatorID\": \"123456789\"\r\n"
			+ "  },\r\n"
			+ "  \"orderInformation\": {\r\n"
			+ "    \"billTo\": {\r\n"
			+ "      \"country\": \"US\",\r\n"
			+ "      \"lastName\": \"VDP\",\r\n"
			+ "      \"address2\": \"Address 2\",\r\n"
			+ "      \"address1\": \"201 S. Division St.\",\r\n"
			+ "      \"postalCode\": \"48104-2201\",\r\n"
			+ "      \"locality\": \"Ann Arbor\",\r\n"
			+ "      \"administrativeArea\": \"MI\",\r\n"
			+ "      \"firstName\": \"RTS\",\r\n"
			+ "      \"phoneNumber\": \"999999999\",\r\n"
			+ "      \"district\": \"MI\",\r\n"
			+ "      \"buildingNumber\": \"123\",\r\n"
			+ "      \"company\": \"Visa\",\r\n"
			+ "      \"email\": \"test@cybs.com\"\r\n"
			+ "    },\r\n"
			+ "    \"amountDetails\": {\r\n"
			+ "      \"totalAmount\": \"102.21\",\r\n"
			+ "      \"currency\": \"USD\"\r\n"
			+ "    }\r\n"
			+ "  },\r\n"
			+ "  \"paymentInformation\": {\r\n"
			+ "    \"card\": {\r\n"
			+ "      \"expirationYear\": \"2031\",\r\n"
			+ "      \"number\": \"5555555555554444\",\r\n"
			+ "      \"securityCode\": \"123\",\r\n"
			+ "      \"expirationMonth\": \"12\",\r\n"
			+ "      \"type\": \"002\"\r\n"
			+ "    }\r\n"
			+ "  }\r\n"
			+ "}";
	
	private String requestTarget = GlobalLabelParameters.REQUEST_TARGET;

	@Before
	public void setUp() {
		apiController = new ApiController();
	}

	// test cases for http
	@Test
	public void PaymentTestForHttpGet() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		requestTarget = requestTarget + getID;
		merchantConfig.setRequestTarget(requestTarget);

		merchantConfig.setRequestType("GET");
		merchantConfig.setAuthenticationType("HTTP_Signature");
		
		Response response = apiController.paymentGet(merchantConfig);
		Assert.assertEquals(200, Integer.parseInt(response.getResponseCode()));
	}

	@Test
	public void PaymentTestForHttpPost() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestData(requestJsonData);

		merchantConfig.setRequestType("POST");
		merchantConfig.setAuthenticationType("HTTP_Signature");
		
		Response response = apiController.paymentPost(merchantConfig);
		Assert.assertEquals(201, Integer.parseInt(response.getResponseCode()));
	}

	// test cases for JWT
	@Test
	public void PaymentTestForJwtGet() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		requestTarget = requestTarget + getID;
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestData(requestJsonData);

		merchantConfig.setRequestType("GET");
		merchantConfig.setAuthenticationType("JWT");
		
		Response response = apiController.paymentGet(merchantConfig);
		Assert.assertEquals(200, Integer.parseInt(response.getResponseCode()));
	}

	@Test
	public void PaymentTestForJwtPost() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);
		merchantConfig.setRequestData(requestJsonData);

		merchantConfig.setRequestType("POST");
		merchantConfig.setAuthenticationType("JWT");
		
		Response response = apiController.paymentPost(merchantConfig);
		Assert.assertEquals(201, Integer.parseInt(response.getResponseCode()));
	}

	// test case for auth type

	@Test
	public void PaymentTestAuthPostFail() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);

		merchantConfig.setRequestType("POST");
		merchantConfig.setAuthenticationType("test");
		
		Response response = apiController.paymentPost(merchantConfig);
		Assert.assertNull(response.getResponseCode());
	}

	@Test
	public void PaymentTestAuthGetFail() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);

		merchantConfig.setRequestType("GET");
		merchantConfig.setAuthenticationType("test");
		
		apiController.paymentGet(merchantConfig);
		Assert.assertEquals(true,merchantConfig.isMerchantDetailsSet());
	}

	// test case for isMerchantDetails

	@Test
	public void PaymentTestIsMerchantDetailsFailForPost() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);

		merchantConfig.setRequestType("POST");
		merchantConfig.setAuthenticationType("JWT");
		
		Response response = apiController.paymentPost(merchantConfig);
		Assert.assertNull(response.getResponseCode());
	}

	@Test
	public void PaymentTestIsMerchantDetailsFailForGet() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);

		merchantConfig.setRequestType("GET");
		merchantConfig.setAuthenticationType("JWT");
		merchantConfig.setKeyAlias("test");

		Response response = apiController.paymentGet(merchantConfig);
		Assert.assertNull(response.getResponseCode());
	}
}
