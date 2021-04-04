package com.cybersource.apisdk.connection.test;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.apisdk.connection.HttpConnection;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.test.resources.UtilityHelpers;

public class HttpConnectionTest {

	@Mock
	HttpConnection httpConnection;
	@Mock
	MerchantConfig merchantConfig;

	private Properties merchantProp;
	private String getID = "5281957982016993203523";
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
	public void setHeaderDataGetTest() throws Exception {

		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setRequestType("GET");
		requestTarget = requestTarget + getID;
		merchantConfig.setRequestTarget(requestTarget);

		httpConnection = new HttpConnection(merchantConfig);
		httpConnection.setHeaderData(merchantConfig.getMerchantID());
		Assert.assertNotNull(merchantConfig);
	}

	@Test
	public void setJsonApplicationTest() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setRequestType("GET");
		requestTarget = requestTarget + getID;
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestData(requestJsonData);

		httpConnection = new HttpConnection(merchantConfig);
		httpConnection.setContentType();
		Assert.assertNotNull(merchantConfig);
	}

	@Test
	public void setSignatureTest() throws Exception {

		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setRequestType("GET");
		requestTarget = requestTarget + getID;
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestData(requestJsonData);

		httpConnection = new HttpConnection(merchantConfig);
		httpConnection.setSignature();
		Assert.assertNotNull(merchantConfig);
	}

	@Test
	public void setRequestMethodTest() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setRequestType("GET");
		requestTarget = requestTarget + getID;
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestData(requestJsonData);

		httpConnection = new HttpConnection(merchantConfig);
		httpConnection.setRequestMethod("GET");
		Assert.assertNotNull(merchantConfig.getRequestType());
	}
}
