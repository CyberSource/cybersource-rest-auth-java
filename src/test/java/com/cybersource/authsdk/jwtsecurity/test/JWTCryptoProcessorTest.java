package com.cybersource.authsdk.jwtsecurity.test;

import java.security.MessageDigest;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.authsdk.cache.Identity;
import com.cybersource.authsdk.cache.MerchantIdentityCache;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.jwtsecurity.CryptoProcessor;
import com.cybersource.authsdk.jwtsecurity.JWTCryptoProcessorImpl;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

public class JWTCryptoProcessorTest {

	@Mock
	CryptoProcessor jwtCryptoProcessor;

	@Mock
	MerchantConfig merchantConfig;

	@Mock
	RequestContext requestContext;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private String requestJsonPath = "src/test/resources/request.json";
	
	private Properties merchantProp;

	@Test
	public void signTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			requestContext = new RequestContext(merchantConfig.getLogger());
			requestContext.setRequestMethodType(GlobalLabelParameters.POST);
			requestContext.setRequestJsonPath(requestJsonPath);

			MessageDigest jwtBody = MessageDigest.getInstance(GlobalLabelParameters.SHA_256);
			byte[] jwtClaimSetBody = jwtBody.digest(requestContext.getRequestBody().getBytes());
			String encryptedSigMessage = Base64.getEncoder().encodeToString(jwtClaimSetBody);

			String claimSet = "{\n            \"digest\":\"" + encryptedSigMessage
					+ "\",\n            \"digestAlgorithm\":\"SHA-256\",\n            \"iat\":\""
					+ ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT) + "\"\n} \n\n";

			HashMap<String, Object> customHeaders = new HashMap<>();
			if (merchantConfig.getMerchantID() != null) {
				customHeaders.put(GlobalLabelParameters.V_C_MERCHANT_ID, merchantConfig.getMerchantID());
				customHeaders.put("v-c-partner-id", merchantConfig.getMerchantID());
			}

			CryptoProcessor jwtCryptoProcessor = JWTCryptoProcessorImpl.getInstance(merchantConfig.getLogger());
			Identity identity = MerchantIdentityCache.getInstance(merchantConfig.getLogger())
					.retrieveP12DataFromCache(merchantConfig);
			String signedToken = jwtCryptoProcessor.sign(claimSet, identity.getRsaPrivateKey(), identity.getX509(),
					customHeaders);
			Assert.assertNotNull(signedToken);

		} else {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void signPayloadClaimsetNullTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			requestContext = new RequestContext(merchantConfig.getLogger());
			requestContext.setRequestMethodType(GlobalLabelParameters.POST);
			requestContext.setRequestJsonPath(requestJsonPath);

			HashMap<String, Object> customHeaders = new HashMap<>();
			if (merchantConfig.getMerchantID() != null) {
				customHeaders.put(GlobalLabelParameters.V_C_MERCHANT_ID, merchantConfig.getMerchantID());
				customHeaders.put("v-c-partner-id", merchantConfig.getMerchantID());
			}

			CryptoProcessor jwtCryptoProcessor = JWTCryptoProcessorImpl.getInstance(merchantConfig.getLogger());
			Identity identity = MerchantIdentityCache.getInstance(merchantConfig.getLogger())
					.retrieveP12DataFromCache(merchantConfig);
			String signedToken = jwtCryptoProcessor.sign(null, identity.getRsaPrivateKey(), identity.getX509(),
					customHeaders);
			Assert.assertNull(signedToken);

		} else {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void signPayloadPrivateKeyNullTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			requestContext = new RequestContext(merchantConfig.getLogger());
			requestContext.setRequestMethodType(GlobalLabelParameters.POST);
			requestContext.setRequestJsonPath(requestJsonPath);

			MessageDigest jwtBody = MessageDigest.getInstance(GlobalLabelParameters.SHA_256);
			byte[] jwtClaimSetBody = jwtBody.digest(requestContext.getRequestBody().getBytes());
			String encryptedSigMessage = Base64.getEncoder().encodeToString(jwtClaimSetBody);

			String claimSet = "{\n            \"digest\":\"" + encryptedSigMessage
					+ "\",\n            \"digestAlgorithm\":\"SHA-256\",\n            \"iat\":\""
					+ ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT) + "\"\n} \n\n";

			HashMap<String, Object> customHeaders = new HashMap<>();
			if (merchantConfig.getMerchantID() != null) {
				customHeaders.put(GlobalLabelParameters.V_C_MERCHANT_ID, merchantConfig.getMerchantID());
				customHeaders.put("v-c-partner-id", merchantConfig.getMerchantID());
			}

			CryptoProcessor jwtCryptoProcessor = JWTCryptoProcessorImpl.getInstance(merchantConfig.getLogger());
			Identity identity = MerchantIdentityCache.getInstance(merchantConfig.getLogger())
					.retrieveP12DataFromCache(merchantConfig);
			String signedToken = jwtCryptoProcessor.sign(claimSet, null, identity.getX509(),
					customHeaders);
			Assert.assertNotNull(signedToken);

		} else {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void signPayloadCertificateNullTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			requestContext = new RequestContext(merchantConfig.getLogger());
			requestContext.setRequestMethodType(GlobalLabelParameters.POST);
			requestContext.setRequestJsonPath(requestJsonPath);

			MessageDigest jwtBody = MessageDigest.getInstance(GlobalLabelParameters.SHA_256);
			byte[] jwtClaimSetBody = jwtBody.digest(requestContext.getRequestBody().getBytes());
			String encryptedSigMessage = Base64.getEncoder().encodeToString(jwtClaimSetBody);

			String claimSet = "{\n            \"digest\":\"" + encryptedSigMessage
					+ "\",\n            \"digestAlgorithm\":\"SHA-256\",\n            \"iat\":\""
					+ ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT) + "\"\n} \n\n";

			HashMap<String, Object> customHeaders = new HashMap<>();
			if (merchantConfig.getMerchantID() != null) {
				customHeaders.put(GlobalLabelParameters.V_C_MERCHANT_ID, merchantConfig.getMerchantID());
				customHeaders.put("v-c-partner-id", merchantConfig.getMerchantID());
			}

			CryptoProcessor jwtCryptoProcessor = JWTCryptoProcessorImpl.getInstance(merchantConfig.getLogger());
			Identity identity = MerchantIdentityCache.getInstance(merchantConfig.getLogger())
					.retrieveP12DataFromCache(merchantConfig);
			String signedToken = jwtCryptoProcessor.sign(claimSet, identity.getRsaPrivateKey(), null, customHeaders);
			Assert.assertNotNull(signedToken);

		} else {
			Assert.assertTrue(true);
		}
	}
}
