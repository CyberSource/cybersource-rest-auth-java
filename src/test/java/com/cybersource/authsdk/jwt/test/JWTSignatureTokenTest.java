package com.cybersource.authsdk.jwt.test;

import java.lang.reflect.Field;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.jwt.JwtSignatureTokenGenerator;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

public class JWTSignatureTokenTest {

	@Mock
	JwtSignatureTokenGenerator jwtSignatureToken;

	@Mock
	MerchantConfig merchantConfig;

	@Mock
	RequestContext requestContext;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private String requestJsonPath = "src/test/resources/request.json";
	private String testJsonPath = "src/test/resources/test.json";
	private Properties merchantProp;

	@Before
	public void resetBeforeSingleton()
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field instance = MerchantConfig.class.getDeclaredField("instance");
		instance.setAccessible(true);
		instance.set(null, null);
		merchantProp = null;
	}

	@Test
	public void getTokenTestForGet() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.GET);

		jwtSignatureToken = new JwtSignatureTokenGenerator(merchantConfig, requestContext);

		String token = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			token = jwtSignatureToken.getToken();
			Assert.assertNotNull(token);
		}

	}

	@Test
	public void getTokenTestForPost() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.POST);
		requestContext.setRequestJsonPath(requestJsonPath);

		jwtSignatureToken = new JwtSignatureTokenGenerator(merchantConfig, requestContext);

		String token = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			token = jwtSignatureToken.getToken();
			Assert.assertNotNull(token);
		}

	}

	@Test
	public void getTokenTestForPut() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.PUT);
		requestContext.setRequestJsonPath(requestJsonPath);

		jwtSignatureToken = new JwtSignatureTokenGenerator(merchantConfig, requestContext);

		String token = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			token = jwtSignatureToken.getToken();
			Assert.assertNotNull(token);
		}

	}

	@Test
	public void getTokenTestForDelete() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.DELETE);
		requestContext.setRequestJsonPath("not required");
		requestContext.setRequestTarget("/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest");
		/* Construct the URL with respect to PUTID. */
		String url = "https://" + merchantConfig.getRequestHost() + requestContext.getRequestTarget();
		/* Set the URL. */
		requestContext.setUrl(url);

		jwtSignatureToken = new JwtSignatureTokenGenerator(merchantConfig, requestContext);

		String token = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			token = jwtSignatureToken.getToken();
			Assert.assertNotNull(token);
		}

	}

	@Test
	public void getTokenTestForEmptyRequestBody() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestJsonPath(testJsonPath);
		requestContext.setRequestMethodType(GlobalLabelParameters.PUT);

		jwtSignatureToken = new JwtSignatureTokenGenerator(merchantConfig, requestContext);

		String token = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token);
		} else {
			token = jwtSignatureToken.getToken();
			Assert.assertNotNull(token);
		}

	}

	@Test
	public void getTokenTestFail() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setKeyFile(null);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType(GlobalLabelParameters.POST);
		requestContext.setRequestJsonPath(requestJsonPath);
        
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertTrue(true);
		} else {
			thrown.expect(ConfigException.class);
			thrown.expectMessage(GlobalLabelParameters.SIG_FAILED);

			jwtSignatureToken = new JwtSignatureTokenGenerator(merchantConfig, requestContext);
			jwtSignatureToken.getToken();
		}
		
	}

	@Test
	public void generateSignatureTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestJsonPath(requestJsonPath);
		requestContext.setRequestMethodType(GlobalLabelParameters.POST);

		String token1 = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(token1);
		} else {
			jwtSignatureToken = new JwtSignatureTokenGenerator(merchantConfig, requestContext);
			jwtSignatureToken.generateSignature();
			merchantConfig.getKeyFile().setLastModified(1528);
			token1 = jwtSignatureToken.generateSignature();
			Assert.assertNotNull(token1);
		}

	}
}
