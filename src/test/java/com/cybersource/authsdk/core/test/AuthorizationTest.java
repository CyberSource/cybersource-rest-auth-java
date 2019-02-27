package com.cybersource.authsdk.core.test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.SignatureGenerationException;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

public class AuthorizationTest {

	@Mock
	MerchantConfig merchantConfig;

	@Mock
	RequestContext requestContext;

	private String requestJsonPath = "src/test/resources/request.json";

	private Properties merchantProp;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void resetBeforeSingleton()
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field instance = MerchantConfig.class.getDeclaredField("instance");
		instance.setAccessible(true);
		instance.set(null, null);
		merchantProp = null;
	}

	@Test
	public void getTokenTestForHttp() throws ConfigException, IOException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.GET);
		requestContext.setRequestJsonPath(requestJsonPath);

		String token = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(token);
		} else {
			token = Authorization.getToken(merchantConfig, requestContext);
			Assert.assertNotNull(token);
		}
	}

	@Test
	public void getTokenTestForJwt() throws ConfigException, IOException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.POST);
		requestContext.setRequestJsonPath(requestJsonPath);

		String token = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			token = Authorization.getToken(merchantConfig, requestContext);
			Assert.assertNotNull(token);
		}
	}

	@Test
	public void getTokenTestFail() throws ConfigException, IOException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType("test");
		requestContext.setRequestJsonPath(requestJsonPath);
		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.REQUEST_VALIDATION_ERROR);
		Authorization.getToken(merchantConfig, requestContext);
	}
}
