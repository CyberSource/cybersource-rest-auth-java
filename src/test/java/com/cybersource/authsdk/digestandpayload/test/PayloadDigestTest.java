package com.cybersource.authsdk.digestandpayload.test;

import java.util.Properties;

import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

import junit.framework.Assert;

public class PayloadDigestTest {

	@Mock
	PayloadDigest payloadDigest;
	@Mock
	MerchantConfig merchantConfig;

	private Properties merchantProp;

	private String requestJsonPath = "src/test/resources/request.json";

	@Test
	public void testDigestGeneration() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setRequestJsonPath(requestJsonPath);
		payloadDigest = new PayloadDigest(merchantConfig);
		String str = payloadDigest.getDigest();
		Assert.assertNotNull(str);
	}

	@Test
	public void testPayloadGeneration() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setRequestJsonPath(requestJsonPath);
		payloadDigest = new PayloadDigest(merchantConfig);
		String str = payloadDigest.getPayLoad();
		Assert.assertNotNull(str);
	}

	@Test
	public void testPayloadGenerationJsonPathNull() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setRequestJsonPath(null);
		payloadDigest = new PayloadDigest(merchantConfig);
		String str = payloadDigest.getPayLoad();
		Assert.assertNull(str);
	}

	@Test
	public void testPayloadGenerationJsonPathEmpty() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setRequestJsonPath("");
		payloadDigest = new PayloadDigest(merchantConfig);
		String str = payloadDigest.getPayLoad();
		Assert.assertNull(str);
	}

	@Test
	public void testPayloadGenerationFail() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setRequestJsonPath("");
		GlobalLabelParameters.SHA_256 = "sh";
		payloadDigest = new PayloadDigest(merchantConfig);
		String str = payloadDigest.getDigest();
		Assert.assertNull(str);
	}

	@Test
	public void getMessageBodyTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestJsonPath(requestJsonPath);

		payloadDigest = new PayloadDigest(merchantConfig);
		String str = payloadDigest.getMessageBody();
		Assert.assertNull(str);
	}

}
