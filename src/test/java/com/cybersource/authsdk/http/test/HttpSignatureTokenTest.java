package com.cybersource.authsdk.http.test;

import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.http.HttpSignatureToken;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

public class HttpSignatureTokenTest {

	@Mock
	HttpSignatureToken signatureToken;
	@Mock
	MerchantConfig merchantConfig;
	@Mock
	Logger logger;
	
	private Properties merchantProp;

	private String requestJsonPath = "src/test/resources/request.json";

	@Test
	public void getTokenTestForGet() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		
		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);
		
		signatureToken = new HttpSignatureToken(merchantConfig);
		String str=null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(str);
		} else {
			str = signatureToken.getToken();
			Assert.assertNotNull(str);
		}
		
	}

	@Test
	public void getTokenTestForPost() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setRequestType("POST");
		
		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);

		signatureToken = new HttpSignatureToken(merchantConfig);

		String str=null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(str);
		} else {
			str = signatureToken.getToken();
			Assert.assertNotNull(str);
		}
	}

	@Test
	public void getTokenTestForPut() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setRequestType("PUT");
		
		signatureToken = new HttpSignatureToken(merchantConfig);

		String str = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(str);
		} else {
			str = signatureToken.getToken();
			Assert.assertNotNull(str);
		}
	}

	@Test
	public void getTokenTestForDelete() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setRequestType("DELETE");
		
		signatureToken = new HttpSignatureToken(merchantConfig);

		String str=null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(str);
		} else {
			str = signatureToken.getToken();
			Assert.assertNotNull(str);
		}
	}

}
