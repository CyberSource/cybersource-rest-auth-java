package com.cybersource.authsdk.http.test;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.http.SignatureGenerator;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

public class SignatureGeneratorTest {

	@Mock
	SignatureGenerator signatureGenerator;

	@Mock
	MerchantConfig merchantConfig;

	@Mock
	RequestContext requestContext;

	private Properties merchantProp;

	private String requestJsonPath = "src/test/resources/request.json";

	@Test
	public void getSignatureParamTestForGet() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.GET);
		requestContext.setRequestJsonPath(requestJsonPath);

		signatureGenerator = new SignatureGenerator(merchantConfig, requestContext);
		String str = null;
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
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.POST);
		requestContext.setRequestJsonPath(requestJsonPath);

		signatureGenerator = new SignatureGenerator(merchantConfig, requestContext);
		String str = null;
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
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.PATCH);
		requestContext.setRequestJsonPath(requestJsonPath);

		signatureGenerator = new SignatureGenerator(merchantConfig, requestContext);
		String str = null;
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
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.PUT);
		requestContext.setRequestJsonPath(requestJsonPath);

		signatureGenerator = new SignatureGenerator(merchantConfig, requestContext);
		String str = null;
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
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestJsonPath(requestJsonPath);
		requestContext.setRequestMethodType("option");

		signatureGenerator = new SignatureGenerator(merchantConfig, requestContext);

		String str = signatureGenerator.getRequestTarget(requestContext.getRequestMethodType());
		Assert.assertNull(str);

	}

}
