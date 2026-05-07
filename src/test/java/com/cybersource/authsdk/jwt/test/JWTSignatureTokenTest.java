package com.cybersource.authsdk.jwt.test;

import java.util.Base64;
import java.util.Properties;

import com.cybersource.authsdk.core.ConfigException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.jwt.JwtSignatureToken;
import com.cybersource.authsdk.util.GlobalLabelParameters;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

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
	private String requestTarget = "/pts/v1/payments";

	@Test
	public void getTokenTestForGet() throws Exception {


		merchantConfig.validateMerchantDetails("GET");

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, requestJsonData, "GET", requestTarget,false);

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

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, requestJsonData, "POST", requestTarget, false);

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

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, requestJsonData, "PUT", requestTarget,false);

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

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, testJsonDataEmpty, "DELETE", requestTarget,false);

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

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, testJsonDataEmpty, "PUT", requestTarget,false);

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

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, requestJsonData, "POST", requestTarget,false);

		String token = jwtSignatureToken.getToken();
		Assert.assertNull(token);
	}

	@Test
	public void generateSignatureTest() throws Exception {

		merchantConfig.validateMerchantDetails("POST");

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, requestJsonData, "POST", requestTarget,false);
		jwtSignatureToken.generateSignature();
		String token1 = jwtSignatureToken.generateSignature();
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token1);
		} else {
			Assert.assertNotNull(token1);
		}
	}

	// ===== JWT SHARED_SECRET (Symmetric / HS256) Tests =====

	private MerchantConfig createSharedSecretConfig() throws ConfigException {
		Properties sharedSecretProps = new Properties();
		sharedSecretProps.setProperty("authenticationType", "jwt");
		sharedSecretProps.setProperty("jwtKeyType", "SHARED_SECRET");
		sharedSecretProps.setProperty("merchantID", "testrest");
		sharedSecretProps.setProperty("runEnvironment", "apitest.cybersource.com");
		sharedSecretProps.setProperty("merchantKeyId", "08c94330-f618-42a3-b09d-e1e43be5efda");
		sharedSecretProps.setProperty("merchantsecretKey", "yBJxy6LjM2TmcPGu+GaJrHtkke25fPpUX+UY6/L/1tE=");
		return new MerchantConfig(sharedSecretProps);
	}

	@Test
	public void getTokenTestSharedSecretForGet() throws Exception {
		MerchantConfig ssConfig = createSharedSecretConfig();
		ssConfig.validateMerchantDetails("GET");

		JwtSignatureToken token = new JwtSignatureToken(ssConfig, "", "GET", requestTarget, false);
		String result = token.getToken();
		Assert.assertNotNull(result);
	}

	@Test
	public void getTokenTestSharedSecretForPost() throws Exception {
		MerchantConfig ssConfig = createSharedSecretConfig();
		ssConfig.validateMerchantDetails("POST");

		JwtSignatureToken token = new JwtSignatureToken(ssConfig, requestJsonData, "POST", requestTarget, false);
		String result = token.getToken();
		Assert.assertNotNull(result);
	}

	@Test
	public void getTokenTestSharedSecretForPut() throws Exception {
		MerchantConfig ssConfig = createSharedSecretConfig();
		ssConfig.validateMerchantDetails("PUT");

		JwtSignatureToken token = new JwtSignatureToken(ssConfig, requestJsonData, "PUT", requestTarget, false);
		String result = token.getToken();
		Assert.assertNotNull(result);
	}

	@Test
	public void getTokenTestSharedSecretForDelete() throws Exception {
		MerchantConfig ssConfig = createSharedSecretConfig();
		ssConfig.validateMerchantDetails("DELETE");

		JwtSignatureToken token = new JwtSignatureToken(ssConfig, "", "DELETE", requestTarget, false);
		String result = token.getToken();
		Assert.assertNotNull(result);
	}

	@Test
	public void getTokenTestSharedSecretForPatch() throws Exception {
		MerchantConfig ssConfig = createSharedSecretConfig();
		ssConfig.validateMerchantDetails("PATCH");

		JwtSignatureToken token = new JwtSignatureToken(ssConfig, requestJsonData, "PATCH", requestTarget, false);
		String result = token.getToken();
		Assert.assertNotNull(result);
	}

	@Test
	public void getTokenTestSharedSecretEmptyBody() throws Exception {
		MerchantConfig ssConfig = createSharedSecretConfig();
		ssConfig.validateMerchantDetails("PUT");

		JwtSignatureToken token = new JwtSignatureToken(ssConfig, "", "PUT", requestTarget, false);
		String result = token.getToken();
		Assert.assertNotNull(result);
	}

	@Test
	public void testSharedSecretTokenIsValidJwt() throws Exception {
		MerchantConfig ssConfig = createSharedSecretConfig();
		ssConfig.validateMerchantDetails("POST");

		JwtSignatureToken token = new JwtSignatureToken(ssConfig, requestJsonData, "POST", requestTarget, false);
		String result = token.getToken();

		// A valid JWS compact serialization has exactly 3 parts separated by '.'
		String[] parts = result.split("\\.");
		Assert.assertEquals("JWT should have 3 parts (header.payload.signature)", 3, parts.length);
	}

	@Test
	public void testSharedSecretTokenHeaderUsesHS256() throws Exception {
		MerchantConfig ssConfig = createSharedSecretConfig();
		ssConfig.validateMerchantDetails("POST");

		JwtSignatureToken token = new JwtSignatureToken(ssConfig, requestJsonData, "POST", requestTarget, false);
		String result = token.getToken();

		String headerJson = new String(Base64.getUrlDecoder().decode(result.split("\\.")[0]));
		JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
		JSONObject header = (JSONObject) parser.parse(headerJson);
		Assert.assertEquals("HS256", header.getAsString("alg"));
	}

	@Test
	public void testSharedSecretTokenHeaderKidIsMerchantKeyId() throws Exception {
		MerchantConfig ssConfig = createSharedSecretConfig();
		ssConfig.validateMerchantDetails("POST");

		JwtSignatureToken token = new JwtSignatureToken(ssConfig, requestJsonData, "POST", requestTarget, false);
		String result = token.getToken();

		String headerJson = new String(Base64.getUrlDecoder().decode(result.split("\\.")[0]));
		JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
		JSONObject header = (JSONObject) parser.parse(headerJson);
		Assert.assertEquals("08c94330-f618-42a3-b09d-e1e43be5efda", header.getAsString("kid"));
	}

	@Test
	public void testP12TokenHeaderUsesRS256() throws Exception {
		// Regression: existing P12 path should still use RS256
		merchantConfig.validateMerchantDetails("POST");

		jwtSignatureToken = new JwtSignatureToken(merchantConfig, requestJsonData, "POST", requestTarget, false);
		String result = jwtSignatureToken.getToken();

		String headerJson = new String(Base64.getUrlDecoder().decode(result.split("\\.")[0]));
		JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
		JSONObject header = (JSONObject) parser.parse(headerJson);
		Assert.assertEquals("RS256", header.getAsString("alg"));
	}

	@Test
	public void testSharedSecretTokenPayloadContainsRequiredClaims() throws Exception {
		MerchantConfig ssConfig = createSharedSecretConfig();
		ssConfig.validateMerchantDetails("GET");

		JwtSignatureToken token = new JwtSignatureToken(ssConfig, "", "GET", requestTarget, false);
		String result = token.getToken();

		String payloadJson = new String(Base64.getUrlDecoder().decode(result.split("\\.")[1]));
		JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
		JSONObject payload = (JSONObject) parser.parse(payloadJson);

		Assert.assertNotNull("Missing 'iat'", payload.get("iat"));
		Assert.assertNotNull("Missing 'exp'", payload.get("exp"));
		Assert.assertNotNull("Missing 'iss'", payload.get("iss"));
		Assert.assertNotNull("Missing 'jti'", payload.get("jti"));
		Assert.assertNotNull("Missing 'request-host'", payload.get("request-host"));
		Assert.assertNotNull("Missing 'request-method'", payload.get("request-method"));
		Assert.assertNotNull("Missing 'v-c-merchant-id'", payload.get("v-c-merchant-id"));
		Assert.assertNotNull("Missing 'v-c-jwt-version'", payload.get("v-c-jwt-version"));
	}

	@Test
	public void testSharedSecretPostTokenPayloadContainsDigest() throws Exception {
		MerchantConfig ssConfig = createSharedSecretConfig();
		ssConfig.validateMerchantDetails("POST");

		JwtSignatureToken token = new JwtSignatureToken(ssConfig, requestJsonData, "POST", requestTarget, false);
		String result = token.getToken();

		String payloadJson = new String(Base64.getUrlDecoder().decode(result.split("\\.")[1]));
		JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
		JSONObject payload = (JSONObject) parser.parse(payloadJson);

		Assert.assertNotNull("POST should contain 'digest'", payload.get("digest"));
		Assert.assertNotNull("POST should contain 'digestAlgorithm'", payload.get("digestAlgorithm"));
	}

	@Test
	public void testSharedSecretGetTokenPayloadNoDigest() throws Exception {
		MerchantConfig ssConfig = createSharedSecretConfig();
		ssConfig.validateMerchantDetails("GET");

		JwtSignatureToken token = new JwtSignatureToken(ssConfig, "", "GET", requestTarget, false);
		String result = token.getToken();

		String payloadJson = new String(Base64.getUrlDecoder().decode(result.split("\\.")[1]));
		JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
		JSONObject payload = (JSONObject) parser.parse(payloadJson);

		Assert.assertNull("GET should NOT contain 'digest'", payload.get("digest"));
	}
}