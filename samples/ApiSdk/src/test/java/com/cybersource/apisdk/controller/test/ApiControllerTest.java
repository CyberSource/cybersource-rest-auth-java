package com.cybersource.apisdk.controller.test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.apisdk.controller.ApiController;
import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.SignatureGenerationException;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

public class ApiControllerTest {

	@Mock
	ApiController apiController;

	@Mock
	MerchantConfig merchantConfig;

	@Mock
	RequestContext requestContext;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Properties merchantProp;
	private String requestJsonPath = "src/test/resources/request.json";
	private String url = "https://";
	private String requestTarget = GlobalLabelParameters.REQUEST_TARGET;
	private Response response;
	Logger logger;

	@Before
	public void setUp()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		apiController = new ApiController();
		Field instance = MerchantConfig.class.getDeclaredField("instance");
		instance.setAccessible(true);
		instance.set(null, null);
		merchantProp = null;
	}

	@Test
	public void PaymentTestForGet() throws IOException, ConfigException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		String requestTarget = "/pts/v2/payments/5383909529456080803003";
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestTarget(requestTarget);
		url = url + merchantConfig.getRequestHost() + requestTarget;
		requestContext.setUrl(url);
		requestContext.setRequestMethodType("GET");
		requestContext.setRequestJsonPath("not required");
		response = apiController.paymentGet(merchantConfig, requestContext);
		Assert.assertNotNull(response.getResponseCode());
	}

	@Test
	public void PaymentTestForPost()
			throws IOException, ConfigException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestTarget(requestTarget);
		requestContext.setRequestJsonPath(requestJsonPath);
		requestContext.setRequestMethodType("POST");
		url = url + merchantConfig.getRequestHost() + requestTarget;
		requestContext.setUrl(url);
		response = apiController.paymentPost(merchantConfig, requestContext);
		Assert.assertNotNull(response.getResponseCode());
	}

	@Test
	public void PaymentTestForPatch()
			throws IOException, ConfigException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestTarget(requestTarget);
		requestContext.setRequestJsonPath(requestJsonPath);
		requestContext.setRequestMethodType("PATCH");
		url = url + merchantConfig.getRequestHost() + requestTarget;
		requestContext.setUrl(url);
		response = apiController.paymentPost(merchantConfig, requestContext);
		Assert.assertNotNull(response.getResponseCode());
	}

	@Test
	public void PaymentTestForPut() throws IOException, ConfigException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestTarget = "/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest";

		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestTarget(requestTarget);
		url = url + merchantConfig.getRequestHost() + requestTarget;
		requestContext.setUrl(url);
		requestJsonPath = "src/test/resources/TRRReport.json";
		requestContext.setRequestJsonPath(requestJsonPath);
		requestContext.setRequestMethodType("PUT");

		response = apiController.paymentPut(merchantConfig, requestContext);
		Assert.assertNotNull(response.getResponseCode());
	}

	@Test
	public void PaymentTestForDelete()
			throws IOException, ConfigException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestTarget = "/reporting/v2/reportSubscriptions/TRRReport?organizationId=testrest";

		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestTarget(requestTarget);
		url = url + merchantConfig.getRequestHost() + requestTarget;
		requestContext.setUrl(url);
		requestContext.setRequestJsonPath("not required");
		requestContext.setRequestMethodType("DELETE");
		response = apiController.paymentDelete(merchantConfig, requestContext);
		Assert.assertNotNull(response.getResponseCode());

	}

	@Test
	public void PaymentTestAuthPostFail()
			throws IOException, ConfigException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setAuthenticationType("");
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);
		requestContext.setRequestJsonPath(requestJsonPath);

		url = url + merchantConfig.getRequestHost() + requestTarget;
		requestContext.setRequestMethodType("POST");

		requestContext.setUrl(url);

		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.AUTH_ERROR);

		response = apiController.paymentPost(merchantConfig, requestContext);
	}

	@Test
	public void validateRequestMethodInvalidTest()
			throws IOException, ConfigException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);
		url = url + merchantConfig.getRequestHost() + requestTarget;
		requestContext.setUrl(url);
		requestContext.setRequestJsonPath(requestJsonPath);
		requestContext.setRequestMethodType("option");

		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.REQUEST_VALIDATION_ERROR);

		apiController.paymentGet(merchantConfig, requestContext);
	}

	@Test
	public void validateRequestJsonPathEmptyTest()
			throws IOException, ConfigException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);
		url = url + merchantConfig.getRequestHost() + requestTarget;
		requestContext.setUrl(url);
		requestContext.setRequestJsonPath("");
		requestContext.setRequestMethodType("POST");

		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.REQUEST_JSON_MIS_NULL);

		apiController.paymentPost(merchantConfig, requestContext);
	}

	@Test
	public void validateRequestJsonPathInvalidTest()
			throws IOException, ConfigException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);
		url = url + merchantConfig.getRequestHost() + requestTarget;
		requestContext.setUrl(url);
		requestContext.setRequestJsonPath("test");
		requestContext.setRequestMethodType("POST");

		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.REQUEST_JSON_ERROR);

		apiController.paymentPost(merchantConfig, requestContext);
	}

	@Test
	public void validateRequestJsonPathNotRequiredTest()
			throws IOException, ConfigException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestTarget(GlobalLabelParameters.REQUEST_TARGET);
		url = url + merchantConfig.getRequestHost() + requestTarget;
		requestContext.setUrl(url);
		requestContext.setRequestJsonPath("not required test");
		requestContext.setRequestMethodType("GET");

		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.REQUEST_JSON_ERROR);

		apiController.paymentGet(merchantConfig, requestContext);
	}

}
