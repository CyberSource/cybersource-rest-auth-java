package com.cybersource.authsdk.http.test;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.http.*;
import com.cybersource.authsdk.util.PropertiesUtil;

public class SignatureGeneratorTest {

	@Mock
	SignatureGenerator signatureGenerator;
	@Mock
	MerchantConfig merchantConfig;

	private Properties merchantProp;

	private String get_ID = "5281957982016993203523";
	private String requestJsonPath = "src/test/resources/request.json";

	@Test
	public void getSignatureParamTestForGet() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");

		signatureGenerator = new SignatureGenerator(merchantConfig, merchantConfig.getMerchantsecretKey(),
				merchantConfig.getMerchantID(), merchantConfig.getRequestType(), get_ID);

		String str = signatureGenerator.getSignatureParameterBase64Encoded();
		Assert.assertNotNull(str);

	}

	@Test
	public void getSignatureParamTestForPost() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestJsonPath(requestJsonPath);

		signatureGenerator = new SignatureGenerator(merchantConfig, merchantConfig.getMerchantsecretKey(),
				merchantConfig.getMerchantID(), merchantConfig.getRequestType(), get_ID);

		String str = signatureGenerator.getSignatureParameterBase64Encoded();
		Assert.assertNotNull(str);

	}

	@Test
	public void getSignatureParamTestForPut() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("PUT");
		merchantConfig.setRequestJsonPath(requestJsonPath);

		signatureGenerator = new SignatureGenerator(merchantConfig, merchantConfig.getMerchantsecretKey(),
				merchantConfig.getMerchantID(), merchantConfig.getRequestType(), get_ID);

		String str = signatureGenerator.getSignatureParameterBase64Encoded();
		Assert.assertNotNull(str);

	}

	@Test
	public void getRequestTargetForNUllTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("option");
		merchantConfig.setRequestJsonPath(requestJsonPath);

		signatureGenerator = new SignatureGenerator(merchantConfig, merchantConfig.getMerchantsecretKey(),
				merchantConfig.getMerchantID(), merchantConfig.getRequestType(), get_ID);

		String str = signatureGenerator.getRequestTarget(merchantConfig.getRequestType());
		Assert.assertNull(str);

	}

}
