package com.cybersource.authsdk.jwt.test;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.jwt.KeyCertificateGenerator;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.test.resources.UtilityHelpers;

public class KeyCertificateGeneratorTest {

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
	@Mock
	KeyCertificateGenerator keyCertificateGenerator;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	MerchantConfig merchantConfig;

	private Properties merchantProp;

	@Test
	public void initializeCertificateTestPass()
			throws CertificateException, NoSuchAlgorithmException, KeyStoreException, Exception {

		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestData(requestJsonData);
		merchantConfig.validateMerchantDetails();

		keyCertificateGenerator = new KeyCertificateGenerator();
		X509Certificate certificate = keyCertificateGenerator.initializeCertificateOfMerchantForJWT(merchantConfig);
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(certificate);
		} else {
			Assert.assertNotNull(certificate);
		}
	}

	@Test
	public void initializePrivateKeyTestPass()
			throws CertificateException, NoSuchAlgorithmException, KeyStoreException, Exception {

		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestData(requestJsonData);
		merchantConfig.validateMerchantDetails();

		keyCertificateGenerator = new KeyCertificateGenerator();
		RSAPrivateKey privateKey = keyCertificateGenerator.initializePrivateKey(merchantConfig);
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(privateKey);
		} else {
			Assert.assertNotNull(privateKey);
		}
	}
}
