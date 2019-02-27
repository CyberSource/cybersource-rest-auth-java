package com.cybersource.authsdk.http.test;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.http.SignatureGenerator;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

public class SignatureGeneratorTest {

	@Mock
	MerchantConfig merchantConfig;

	@Mock
	RequestContext requestContext;

	private Properties merchantProp;

	private String requestJsonPath = "src/test/resources/request.json";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void getSignatureParamTestForGet() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.GET);
		requestContext.setRequestJsonPath(requestJsonPath);

		String str = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(str);
		} else {
			str = SignatureGenerator.generateSignature(merchantConfig, requestContext);
			Assert.assertNotNull(str);
		}

	}

	@Test
	public void getSignatureParamTestForPost() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.POST);
		requestContext.setRequestJsonPath(requestJsonPath);

		String str = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(str);
		} else {
			str = SignatureGenerator.generateSignature(merchantConfig, requestContext);
			Assert.assertNotNull(str);
		}

	}

	@Test
	public void getSignatureParamTestForPatch() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.PATCH);
		requestContext.setRequestJsonPath(requestJsonPath);

		String str = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(str);
		} else {
			str = SignatureGenerator.generateSignature(merchantConfig, requestContext);
			Assert.assertNotNull(str);
		}

	}

	@Test
	public void getSignatureParamTestForPut() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.PUT);
		requestContext.setRequestJsonPath(requestJsonPath);

		String str = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(str);
		} else {
			str = SignatureGenerator.generateSignature(merchantConfig, requestContext);
			Assert.assertNotNull(str);
		}

	}

	@Test
	public void getRequestTargetForNullTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestJsonPath(requestJsonPath);
		requestContext.setRequestMethodType("option");

		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.REQUEST_VALIDATION_ERROR);

		SignatureGenerator.generateSignature(merchantConfig, requestContext);
	}

}
