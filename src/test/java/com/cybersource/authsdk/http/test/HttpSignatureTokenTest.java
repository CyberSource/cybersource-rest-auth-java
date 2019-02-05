package com.cybersource.authsdk.http.test;

import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.authsdk.core.AuthenticationRequestContext;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.http.HttpSignatureTokenGenerator;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

public class HttpSignatureTokenTest {

	@Mock
	HttpSignatureTokenGenerator signatureToken;
	
	@Mock
	MerchantConfig merchantConfig;
	
	@Mock
	AuthenticationRequestContext merchantUtil;
	
	private Properties merchantProp;

	private String requestJsonPath = "src/test/resources/request.json";

//	@Test
//	public void getTokenTestForGet() throws ConfigException, IOException {
//
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType(GlobalLabelParameters.GET);
//		merchantUtil.setRequestJsonPath(requestJsonPath);
//		merchantUtil.getRequestData();
//		merchantConfig.setMerchantDetails();
//		
//		AuthenticationRequestContext.validateMerchantDetails();
//		
//		signatureToken = new HttpSignatureTokenGenerator(merchantConfig,null);
//		String str=null;
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(str);
//		} else {
//			str = signatureToken.getToken();
//			Assert.assertNotNull(str);
//		}
//		
//	}
//
//	@Test
//	public void getTokenTestForPost() throws ConfigException, IOException {
//
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestJsonPath(requestJsonPath);
//		merchantUtil.setRequestType(GlobalLabelParameters.POST);
//		
//		AuthenticationRequestContext.validateMerchantDetails();
//
//		signatureToken = new HttpSignatureTokenGenerator(merchantConfig,merchantUtil.getRequestData());
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
//	public void getTokenTestForPut() throws ConfigException, IOException {
//
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestJsonPath(requestJsonPath);
//		merchantUtil.setRequestType(GlobalLabelParameters.PUT);
//		
//		signatureToken = new HttpSignatureTokenGenerator(merchantConfig,merchantUtil.getRequestData());
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
//	public void getTokenTestForDelete() throws ConfigException, IOException {
//
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestJsonPath(requestJsonPath);
//		merchantUtil.setRequestType("DELETE");
//		
//		signatureToken = new HttpSignatureTokenGenerator(merchantConfig,merchantUtil.getRequestData());
//
//		String str=null;
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(str);
//		} else {
//			str = signatureToken.getToken();
//			Assert.assertNotNull(str);
//		}
//	}

}
