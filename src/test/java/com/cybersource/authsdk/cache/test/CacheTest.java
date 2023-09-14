package com.cybersource.authsdk.cache.test;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.authsdk.cache.Cache;
import com.cybersource.authsdk.cache.Identity;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.test.resources.UtilityHelpers;

import junit.framework.Assert;

public class CacheTest {

	private Properties merchantProp;
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
	public static ConcurrentHashMap<String, Identity> cacheP12 = new ConcurrentHashMap<>();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	Cache cache;
	@Mock
	MerchantConfig merchantConfig;

	@Test
	public void setP12FileDetailsInCacheEmptyTest() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestData(requestJsonData);

		merchantConfig.validateMerchantDetails();
		merchantConfig.checkKeyFile();

		cache = new Cache(merchantConfig);
		cache.setP12FileDetailsInCache();
		Assert.assertNotNull(cacheP12);
	}

	@Test
	public void setP12FileDetailsInCacheNotEmptyTest() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestData(requestJsonData);

		merchantConfig.validateMerchantDetails();
		merchantConfig.checkKeyFile();

		cache = new Cache(merchantConfig);
		cache.setUpP12Cache();
		cache.setP12FileDetailsInCache();
		Assert.assertNotNull(cacheP12);
	}

	@Test
	public void setP12FileDetailsInCacheTimeStampTest() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestData(requestJsonData);

		merchantConfig.validateMerchantDetails();
		merchantConfig.checkKeyFile();

		cache = new Cache(merchantConfig);
		cache.setUpP12Cache();
		Cache.lastModifiedTime = 1528;
		cache.setP12FileDetailsInCache();
		Assert.assertNotNull(cacheP12);
	}

	@Test
	public void testGetLastModifiedTimeThrowsExceptionWhenPathIsNULL() throws Exception {
		merchantProp = UtilityHelpers.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		Cache cache = new Cache(merchantConfig);
		thrown.expect(Exception.class);
		cache.getLastModifiedFileTime(null);
	}
}
