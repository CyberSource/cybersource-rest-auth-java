package com.cybersource.authsdk.http.test;

import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.authsdk.core.RequestContext;
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
	RequestContext requestContext;

	private Properties merchantProp;

	private String requestJsonPath = "src/test/resources/request.json";

	@Test
	public void getTokenTestForGet() throws ConfigException, IOException {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.GET);
		requestContext.setRequestJsonPath(requestJsonPath);
		requestContext.getRequestBody();

		signatureToken = new HttpSignatureTokenGenerator(merchantConfig, requestContext);
		String str = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(str);
		} else {
			str = signatureToken.getToken();
			Assert.assertNotNull(str);
		}

	}

	@Test
	public void getTokenTestForPost() throws ConfigException, IOException {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestJsonPath(requestJsonPath);
		requestContext.setRequestMethodType(GlobalLabelParameters.POST);

		signatureToken = new HttpSignatureTokenGenerator(merchantConfig, requestContext);

		String str = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(str);
		} else {
			str = signatureToken.getToken();
			Assert.assertNotNull(str);
		}
	}

	@Test
	public void getTokenTestForPut() throws ConfigException, IOException {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestJsonPath(requestJsonPath);
		requestContext.setRequestMethodType(GlobalLabelParameters.PUT);

		signatureToken = new HttpSignatureTokenGenerator(merchantConfig, requestContext);

		String str = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(str);
		} else {
			str = signatureToken.getToken();
			Assert.assertNotNull(str);
		}
	}

	@Test
	public void getTokenTestForDelete() throws ConfigException, IOException {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestJsonPath(requestJsonPath);
		requestContext.setRequestMethodType("DELETE");

		signatureToken = new HttpSignatureTokenGenerator(merchantConfig, requestContext);

		String str = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(str);
		} else {
			str = signatureToken.getToken();
			Assert.assertNotNull(str);
		}
	}

}
