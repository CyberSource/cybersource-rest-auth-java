package com.cybersource.authsdk.jwt.test;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.authsdk.cache.Cache;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.jwt.JwtSignatureToken;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.test.resources.UtilityHelpers;

public class JWTSignatureTokenTest {

	@Mock
	JwtSignatureToken jwtSignatureToken;
	@Mock
	MerchantConfig merchantConfig;

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
	private String testJsonDataEmpty = "";
	private Properties merchantProp;

	@Test
	public void getTokenTestForGet() throws Exception {

		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		merchantConfig.validateMerchantDetails();

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantConfig.getRequestData());

		String token = jwtSignatureToken.getToken();
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			Assert.assertNotNull(token);
		}
	}

	@Test
	public void getTokenTestForPost() throws Exception {

		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestData(requestJsonData);
		merchantConfig.validateMerchantDetails();

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantConfig.getRequestData());

		String token = jwtSignatureToken.getToken();
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			Assert.assertNotNull(token);
		}
	}

	@Test
	public void getTokenTestForPut() throws Exception {

		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("PUT");
		merchantConfig.setRequestData(requestJsonData);
		merchantConfig.validateMerchantDetails();

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantConfig.getRequestData());

		String token = jwtSignatureToken.getToken();
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			Assert.assertNotNull(token);
		}
	}

	@Test
	public void getTokenTestForDelete() throws Exception {

		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("DELETE");
		merchantConfig.validateMerchantDetails();

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantConfig.getRequestData());

		String token = jwtSignatureToken.getToken();
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			Assert.assertNotNull(token);
		}
	}

	@Test
	public void getTokenTestForEmptyRequestBody() throws Exception {

		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("PUT");
		merchantConfig.setRequestData(testJsonDataEmpty);
		merchantConfig.validateMerchantDetails();

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantConfig.getRequestData());

		String token = jwtSignatureToken.getToken();
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			Assert.assertNotNull(token);
		}
	}

	@Test
	public void getTokenTestFail() throws Exception {

		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestData(requestJsonData);
		merchantConfig.setAuthenticationType("abc");
		merchantConfig.validateMerchantDetails();

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantConfig.getRequestData());

		String token = jwtSignatureToken.getToken();
		Assert.assertNull(token);
	}

	@Test
	public void generateSignatureTest() throws Exception {

		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestData(requestJsonData);
		merchantConfig.validateMerchantDetails();

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, merchantConfig.getRequestData());
		jwtSignatureToken.generateSignature();
		Cache.lastModifiedTime = 1528;
		String token1 = jwtSignatureToken.generateSignature();
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token1);
		} else {
			Assert.assertNotNull(token1);
		}
	}
}
