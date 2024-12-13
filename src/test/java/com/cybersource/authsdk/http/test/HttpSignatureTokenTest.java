//package com.cybersource.authsdk.http.test;
//
//import java.util.Properties;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.mockito.Mock;
//
//import com.cybersource.authsdk.core.MerchantConfig;
//import com.cybersource.authsdk.http.HttpSignatureToken;
//import com.cybersource.authsdk.util.GlobalLabelParameters;
//import com.cybersource.authsdk.test.resources.UtilityHelpers;
//
//public class HttpSignatureTokenTest {
//
//	@Mock
//	HttpSignatureToken signatureToken;
//	@Mock
//	MerchantConfig merchantConfig;
//
//	private Properties merchantProp;
//
//	private String requestJsonData = "{\r\n"
//			+ "  \"clientReferenceInformation\": {\r\n"
//			+ "    \"code\": \"TC50171_3\"\r\n"
//			+ "  },\r\n"
//			+ "  \"processingInformation\": {\r\n"
//			+ "    \"commerceIndicator\": \"internet\"\r\n"
//			+ "  },\r\n"
//			+ "  \"aggregatorInformation\": {\r\n"
//			+ "    \"subMerchant\": {\r\n"
//			+ "      \"cardAcceptorID\": \"1234567890\",\r\n"
//			+ "      \"country\": \"US\",\r\n"
//			+ "      \"phoneNumber\": \"650-432-0000\",\r\n"
//			+ "      \"address1\": \"900 Metro Center\",\r\n"
//			+ "      \"postalCode\": \"94404-2775\",\r\n"
//			+ "      \"locality\": \"Foster City\",\r\n"
//			+ "      \"name\": \"Visa Inc\",\r\n"
//			+ "      \"administrativeArea\": \"CA\",\r\n"
//			+ "      \"region\": \"PEN\",\r\n"
//			+ "      \"email\": \"test@cybs.com\"\r\n"
//			+ "    },\r\n"
//			+ "    \"name\": \"V-Internatio\",\r\n"
//			+ "    \"aggregatorID\": \"123456789\"\r\n"
//			+ "  },\r\n"
//			+ "  \"orderInformation\": {\r\n"
//			+ "    \"billTo\": {\r\n"
//			+ "      \"country\": \"US\",\r\n"
//			+ "      \"lastName\": \"VDP\",\r\n"
//			+ "      \"address2\": \"Address 2\",\r\n"
//			+ "      \"address1\": \"201 S. Division St.\",\r\n"
//			+ "      \"postalCode\": \"48104-2201\",\r\n"
//			+ "      \"locality\": \"Ann Arbor\",\r\n"
//			+ "      \"administrativeArea\": \"MI\",\r\n"
//			+ "      \"firstName\": \"RTS\",\r\n"
//			+ "      \"phoneNumber\": \"999999999\",\r\n"
//			+ "      \"district\": \"MI\",\r\n"
//			+ "      \"buildingNumber\": \"123\",\r\n"
//			+ "      \"company\": \"Visa\",\r\n"
//			+ "      \"email\": \"test@cybs.com\"\r\n"
//			+ "    },\r\n"
//			+ "    \"amountDetails\": {\r\n"
//			+ "      \"totalAmount\": \"102.21\",\r\n"
//			+ "      \"currency\": \"USD\"\r\n"
//			+ "    }\r\n"
//			+ "  },\r\n"
//			+ "  \"paymentInformation\": {\r\n"
//			+ "    \"card\": {\r\n"
//			+ "      \"expirationYear\": \"2031\",\r\n"
//			+ "      \"number\": \"5555555555554444\",\r\n"
//			+ "      \"securityCode\": \"123\",\r\n"
//			+ "      \"expirationMonth\": \"12\",\r\n"
//			+ "      \"type\": \"002\"\r\n"
//			+ "    }\r\n"
//			+ "  }\r\n"
//			+ "}";
//
//	@Test
//	public void getTokenTestForGet() throws Exception {
//		merchantProp = UtilityHelpers.getMerchantProperties();
//		merchantConfig = new MerchantConfig(merchantProp);
//		merchantConfig.setRequestType("GET");
//		merchantConfig.validateMerchantDetails();
//
//		signatureToken = new HttpSignatureToken(merchantConfig);
//		String str=null;
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(str);
//		} else {
//			str = signatureToken.getToken();
//			Assert.assertNotNull(str);
//		}
//	}
//
//	@Test
//	public void getTokenTestForPost() throws Exception {
//		merchantProp = UtilityHelpers.getMerchantProperties();
//		merchantConfig = new MerchantConfig(merchantProp);
//
//		merchantConfig.setRequestData(requestJsonData);
//		merchantConfig.setRequestType("POST");
//		merchantConfig.validateMerchantDetails();
//
//		signatureToken = new HttpSignatureToken(merchantConfig);
//
//		String str=null;
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(str);
//		} else {
//			str = signatureToken.getToken();
//			Assert.assertNotNull(str);
//		}
//	}
//
//	@Test
//	public void getTokenTestForPut() throws Exception {
//		merchantProp = UtilityHelpers.getMerchantProperties();
//		merchantConfig = new MerchantConfig(merchantProp);
//		merchantConfig.setRequestData(requestJsonData);
//		merchantConfig.setRequestType("PUT");
//
//		signatureToken = new HttpSignatureToken(merchantConfig);
//
//		String str = null;
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(str);
//		} else {
//			str = signatureToken.getToken();
//			Assert.assertNotNull(str);
//		}
//	}
//
//	@Test
//	public void getTokenTestForDelete() throws Exception {
//		merchantProp = UtilityHelpers.getMerchantProperties();
//		merchantConfig = new MerchantConfig(merchantProp);
//		merchantConfig.setRequestData(requestJsonData);
//		merchantConfig.setRequestType("DELETE");
//
//		signatureToken = new HttpSignatureToken(merchantConfig);
//
//		String str=null;
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(str);
//		} else {
//			str = signatureToken.getToken();
//			Assert.assertNotNull(str);
//		}
//	}
//}
