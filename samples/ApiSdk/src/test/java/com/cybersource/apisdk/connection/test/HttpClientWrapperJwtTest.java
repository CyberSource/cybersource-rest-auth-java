package com.cybersource.apisdk.connection.test;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.apisdk.connection.HttpClientWrapper;
import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.SignatureGenerationException;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

public class HttpClientWrapperJwtTest {

	@Mock
	HttpClientWrapper httpClientWrapper;

	@Mock
	MerchantConfig merchantConfig;

	@Mock
	RequestContext requestContext;

	private Properties merchantProp;
	private String id = "5281957982016993203523";
	private String url = "https://";
	private String requestTarget = GlobalLabelParameters.REQUEST_TARGET;
	private String requestJsonPath = "src/test/resources/request.json";
	private Response response;

	@Mock
	Logger logger;

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void setHeaderDataTestForGet() throws ConfigException, IOException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext=new RequestContext(merchantConfig.getLogger());
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(null);
		} else {
			requestTarget = requestTarget + id;
	        requestContext.setRequestTarget(requestTarget);
			url = url + merchantConfig.getRequestHost() + requestContext.getRequestTarget();
			requestContext.setUrl(url);
			requestContext.setRequestMethodType("GET");
			requestContext.setRequestJsonPath(requestJsonPath);
			httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig);
			Map<String, String> headerMap = httpClientWrapper.getHttpHeader(requestContext);
			Assert.assertNotNull(headerMap);
		}
	}
	
	@Test
	public void setHeaderDataTestForDelete() throws ConfigException, IOException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext=new RequestContext(merchantConfig.getLogger());
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(null);
		} else {
			requestTarget = requestTarget + id;
	        requestContext.setRequestTarget(requestTarget);
			url = url + merchantConfig.getRequestHost() + requestContext.getRequestTarget();
			requestContext.setUrl(url);
			requestContext.setRequestMethodType("DELETE");
			requestContext.setRequestJsonPath(requestJsonPath);
			httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig);
			Map<String, String> headerMap = httpClientWrapper.getHttpHeader(requestContext);
			Assert.assertNotNull(headerMap);
		}
	}
	
	@Test
	public void setHeaderDataTestForPost() throws ConfigException, SignatureGenerationException, IOException, ClientException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext=new RequestContext(merchantConfig.getLogger());
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(null);
		} else {
			requestContext.setRequestTarget(requestTarget);
			url = url + merchantConfig.getRequestHost() + requestContext.getRequestTarget();
			requestContext.setUrl(url);
			requestContext.setRequestMethodType("POST");
			requestContext.setRequestJsonPath(requestJsonPath);
			httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig);
			Map<String, String> headerMap = httpClientWrapper.getHttpHeader(requestContext);
			Assert.assertNotNull(headerMap);
		}
	}
	
	@Test
	public void setHeaderDataTestForPatch() throws ConfigException, IOException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext=new RequestContext(merchantConfig.getLogger());
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(null);
		} else {
			requestContext.setRequestTarget(requestTarget);
			url = url + merchantConfig.getRequestHost() + requestContext.getRequestTarget();
			requestContext.setUrl(url);
			requestContext.setRequestMethodType("PATCH");
			requestContext.setRequestJsonPath(requestJsonPath);
			httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig);
			Map<String, String> headerMap = httpClientWrapper.getHttpHeader(requestContext);
			Assert.assertNotNull(headerMap);
		}
	}

	@Test
	public void getTestForGet() throws ClientException, IOException, ConfigException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestTarget = requestTarget + id;
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestTarget(requestTarget);
		requestContext.setRequestJsonPath(requestJsonPath);
		requestContext.setRequestMethodType("GET");

		url = url + merchantConfig.getRequestHost() + requestTarget;
		requestContext.setUrl(url);

		logger = merchantConfig.getLogger();

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(null);
		} else {
			httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig);
			Response str = httpClientWrapper.sendRequest(requestContext);
			Assert.assertNotNull(str);
		}
	}

	@Test
	public void getTestForPost() throws ClientException, IOException, ConfigException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestTarget(requestTarget);
		requestContext.setRequestJsonPath(requestJsonPath);
		requestContext.setRequestMethodType("POST");

		url = url + merchantConfig.getRequestHost() + requestTarget;
		requestContext.setUrl(url);

		logger = merchantConfig.getLogger();

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(null);
		} else {
			httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig);
			Response resp = httpClientWrapper.sendRequest(requestContext);
			Assert.assertNotNull(resp);
		}
	}

	@Test
	public void getTestForPut() throws IOException, ConfigException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestTarget(requestTarget);
		requestContext.setRequestJsonPath(requestJsonPath);
		requestContext.setRequestMethodType("PUT");

		url = url + merchantConfig.getRequestHost() + requestTarget;
		requestContext.setUrl(url);

		logger = merchantConfig.getLogger();

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(null);
		} else {
			httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig);
			response = httpClientWrapper.sendRequest(requestContext);
			Assert.assertNotNull(response);
		}

	}

	@Test
	public void getTestForPatch() throws IOException, ConfigException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestTarget(requestTarget);
		requestContext.setRequestJsonPath(requestJsonPath);
		requestContext.setRequestMethodType("PATCH");

		url = url + merchantConfig.getRequestHost() + requestTarget;
		requestContext.setUrl(url);

		logger = merchantConfig.getLogger();

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(null);
		} else {
			httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig);
			response = httpClientWrapper.sendRequest(requestContext);
			Assert.assertNotNull(response);
		}
	}

	@Test
	public void openConnectionEmptyUrlTest()
			throws ClientException, IOException, ConfigException, SignatureGenerationException {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestTarget = requestTarget + id;
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestTarget(requestTarget);
		requestContext.setUrl("");
		requestContext.setRequestJsonPath(requestJsonPath);
		requestContext.setRequestMethodType("GET");

		logger = merchantConfig.getLogger();

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(null);
		} else {
			thrown.expect(ClientException.class);
			thrown.expectMessage(GlobalLabelParameters.URL_NOT_SET);
			httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig);
			httpClientWrapper.sendRequest(requestContext);
		}

	}

}
