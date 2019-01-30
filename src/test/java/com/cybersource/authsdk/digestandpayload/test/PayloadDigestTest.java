package com.cybersource.authsdk.digestandpayload.test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;

import junit.framework.Assert;

public class PayloadDigestTest {

	@Mock
	PayloadDigest payloadDigest;
	@Mock
	MerchantConfig merchantConfig;
	
	@Mock
	MerchantUtil merchantUtil;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Properties merchantProp;
    private String requestJsonPath = "src/test/resources/request.json";

	@Test
	public void digestGenerationTest() throws ConfigException, IOException, NoSuchAlgorithmException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		payloadDigest = new PayloadDigest(merchantUtil.getRequestData());
		String str = payloadDigest.getDigest();
		Assert.assertNotNull(str);
	}

	@Test
	public void payloadGenerationTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		payloadDigest = new PayloadDigest(merchantUtil.getRequestData());
		String str = payloadDigest.getPayLoad();
		Assert.assertNotNull(str);
	}

	@Test
	public void payloadGenerationJsonPathNullTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(null);
		
		payloadDigest = new PayloadDigest(merchantUtil.getRequestData());
		String str = payloadDigest.getPayLoad();
		Assert.assertNull(str);
	}

	@Test
	public void payloadGenerationJsonPathEmptyTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath("");
		payloadDigest = new PayloadDigest(merchantUtil.getRequestData());
		String str = payloadDigest.getPayLoad();
		Assert.assertNull(str);
	}


}
