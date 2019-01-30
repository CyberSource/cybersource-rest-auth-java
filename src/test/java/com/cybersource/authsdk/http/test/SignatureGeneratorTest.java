package com.cybersource.authsdk.http.test;

import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.http.SignatureGenerator;
import com.cybersource.authsdk.log.AuthLog4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;

public class SignatureGeneratorTest {

	@Mock
	SignatureGenerator signatureGenerator;
	@Mock
	MerchantConfig merchantConfig;
	@Mock
	MerchantUtil merchantUtil;
	@Mock
	Logger logger;

	private Properties merchantProp;

	private String requestJsonPath = "src/test/resources/request.json";

	@Test
	public void getSignatureParamTestForGet() throws Exception {
		
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestType(GlobalLabelParameters.GET);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantUtil.getRequestData();
		merchantConfig.setMerchantDetails();
		
		logger = merchantConfig.getLogger();
		MerchantUtil.validateMerchantDetails();
		
		signatureGenerator = new SignatureGenerator(merchantConfig,null, GlobalLabelParameters.GET);
		String str=null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(str);
		} else {
			str = signatureGenerator.generateSignature();
			Assert.assertNotNull(str);
		}

	}

	@Test
	public void getSignatureParamTestForPost() throws Exception {
		
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestType(GlobalLabelParameters.POST);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantConfig.setMerchantDetails();
		
		logger = merchantConfig.getLogger();
		MerchantUtil.validateMerchantDetails();
		
		signatureGenerator = new SignatureGenerator(merchantConfig,merchantUtil.getRequestData(), GlobalLabelParameters.POST);
		String str=null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(str);
		} else {
			str = signatureGenerator.generateSignature();
			Assert.assertNotNull(str);
		}

	}
	
	@Test
	public void getSignatureParamTestForPatch() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestType(GlobalLabelParameters.PATCH);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantConfig.setMerchantDetails();
		
		logger = merchantConfig.getLogger();
		MerchantUtil.validateMerchantDetails();
		
		signatureGenerator = new SignatureGenerator(merchantConfig,merchantUtil.getRequestData(), GlobalLabelParameters.PATCH);
		String str=null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(str);
		} else {
			str = signatureGenerator.generateSignature();
			Assert.assertNotNull(str);
		}

	}

	@Test
	public void getSignatureParamTestForPut() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestType(GlobalLabelParameters.PUT);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantConfig.setMerchantDetails();
		
		logger = merchantConfig.getLogger();
		MerchantUtil.validateMerchantDetails();
		
		signatureGenerator = new SignatureGenerator(merchantConfig,merchantUtil.getRequestData(),GlobalLabelParameters.PUT);
		String str=null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(str);
		} else {
			str = signatureGenerator.generateSignature();
			Assert.assertNotNull(str);
		}

	}

	@Test
	public void getRequestTargetForNUllTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantUtil.setRequestType("option");

		signatureGenerator = new SignatureGenerator(merchantConfig,merchantUtil.getRequestData(),GlobalLabelParameters.GET);

		String str = signatureGenerator.getRequestTarget(MerchantUtil.getRequestType());
		Assert.assertNull(str);

	}

}
