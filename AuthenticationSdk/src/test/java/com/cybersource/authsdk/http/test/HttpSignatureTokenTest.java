package com.cybersource.authsdk.http.test;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.http.HttpSignatureToken;
import com.cybersource.authsdk.util.PropertiesUtil;

public class HttpSignatureTokenTest {

	@Mock
	HttpSignatureToken signatureToken;
	@Mock
	MerchantConfig merchantConfig;

	private Properties merchantProp;

	private String requestJsonPath = "src/test/resources/request.json";

	@Test
	public void getTokenTestForGet() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		merchantConfig.setAuthenticationType("Http_signature");

		signatureToken = new HttpSignatureToken(merchantConfig);

		String str = signatureToken.getToken();
		Assert.assertNotNull(str);
	}

	@Test
	public void getTokenTestForPost() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setRequestType("POST");
		merchantConfig.setAuthenticationType("Http_signature");

		signatureToken = new HttpSignatureToken(merchantConfig);

		String str = signatureToken.getToken();
		Assert.assertNotNull(str);
	}

	@Test
	public void getTokenTestForPut() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setRequestType("PUT");
		merchantConfig.setAuthenticationType("Http_signature");

		signatureToken = new HttpSignatureToken(merchantConfig);

		String str = signatureToken.getToken();
		Assert.assertNotNull(str);
	}

	@Test
	public void getTokenTestForDelete() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setRequestType("DELETE");
		merchantConfig.setAuthenticationType("Http_signature");

		signatureToken = new HttpSignatureToken(merchantConfig);

		String str = signatureToken.getToken();
		Assert.assertNotNull(str);
	}

}
