package com.cybersource.apisdk.connection.test;

import java.util.Properties;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.apisdk.connection.JwtConnection;
import com.cybersource.apisdk.model.Response;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.test.resources.UtilityHelpers;

import junit.framework.Assert;

public class JwtUrlConnectionTest {

	@Mock
	JwtConnection jwtUrlConnection;

	@Mock
	MerchantConfig merchantConfig;

	private Properties merchantProp;
	private String get_ID = "5281957982016993203523";
	private String requestTarget = GlobalLabelParameters.REQUEST_TARGET;
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

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void getTestForGet() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		requestTarget = requestTarget + get_ID;
		merchantConfig.setRequestTarget(requestTarget);

		merchantConfig.validateMerchantDetails();

		jwtUrlConnection = new JwtConnection(merchantConfig);
		Response str = jwtUrlConnection.jwtConnection();
		Assert.assertNotNull(str);
	}

	@Test
	public void getTestForPost() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestData(requestJsonData);

		merchantConfig.validateMerchantDetails();

		jwtUrlConnection = new JwtConnection(merchantConfig);
		Response resp = jwtUrlConnection.postDocument(merchantConfig.getRequestData());
		Assert.assertNotNull(resp);
	}

	@Test
	public void getTestForPut() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("PUT");
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestData(requestJsonData);

		merchantConfig.validateMerchantDetails();

		thrown.expect(Exception.class);

		jwtUrlConnection = new JwtConnection(merchantConfig);
		jwtUrlConnection.postDocument(merchantConfig.getRequestData());
	}

	@Test
	public void setRequestMethodTest() throws Exception {

		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		requestTarget = requestTarget + get_ID;
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestData(requestJsonData);

		merchantConfig.validateMerchantDetails();

		jwtUrlConnection = new JwtConnection(merchantConfig);
		jwtUrlConnection.setRequestMethod("GET");
		Assert.assertNotNull(merchantConfig.getRequestType());
	}
}
