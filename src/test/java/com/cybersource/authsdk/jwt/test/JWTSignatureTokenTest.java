package com.cybersource.authsdk.jwt.test;

import java.util.Properties;

import com.cybersource.authsdk.core.ConfigException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.jwt.JwtSignatureToken;
import com.cybersource.authsdk.util.GlobalLabelParameters;

public class JWTSignatureTokenTest {

	@Mock
	JwtSignatureToken jwtSignatureToken;
    @Mock
    MerchantConfig merchantConfig;

    private Properties props = new Properties();

    @Before
    public void setUp() throws ConfigException {

        props.setProperty("authenticationType", "jwt");
        props.setProperty("merchantID", "testrest");
        props.setProperty("runEnvironment", "apitest.cybersource.com");

		props.setProperty("keyAlias", "testrest");
		props.setProperty("keyPass", "testrest");
		props.setProperty("keyFileName", "testrest");

		props.setProperty("keysDirectory", "src/main/resources");

        merchantConfig = new MerchantConfig(props);
    }

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

	@Test
	public void getTokenTestForGet() throws Exception {


		merchantConfig.validateMerchantDetails("GET");

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, requestJsonData, "GET");

		String token = jwtSignatureToken.getToken();

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			Assert.assertNotNull(token);
		}
	}

	@Test
	public void getTokenTestForPost() throws Exception {

		merchantConfig.validateMerchantDetails("POST");

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, requestJsonData, "POST");

		String token = jwtSignatureToken.getToken();
		System.out.println("token "  + token);
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			Assert.assertNotNull(token);
		}
	}

	@Test
	public void getTokenTestForPut() throws Exception {

		merchantConfig.validateMerchantDetails("PUT");

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, requestJsonData, "PUT");

		String token = jwtSignatureToken.getToken();
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			Assert.assertNotNull(token);
		}
	}

	@Test
	public void getTokenTestForDelete() throws Exception {


		merchantConfig.validateMerchantDetails("DELETE");

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, testJsonDataEmpty, "DELETE");

		String token = jwtSignatureToken.getToken();
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			Assert.assertNotNull(token);
		}
	}

	@Test
	public void getTokenTestForEmptyRequestBody() throws Exception {

		merchantConfig.validateMerchantDetails("PUT");

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, testJsonDataEmpty, "PUT");

		String token = jwtSignatureToken.getToken();
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			Assert.assertNotNull(token);
		}
	}

	@Test
	public void getTokenTestFail() throws Exception {


		merchantConfig.setAuthenticationType("abc");

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, requestJsonData, "POST");

		String token = jwtSignatureToken.getToken();
		Assert.assertNull(token);
	}

	@Test
	public void generateSignatureTest() throws Exception {

		merchantConfig.validateMerchantDetails("POST");

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, requestJsonData, "POST");
		jwtSignatureToken.generateSignature();
		String token1 = jwtSignatureToken.generateSignature();
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token1);
		} else {
			Assert.assertNotNull(token1);
		}
	}
}
