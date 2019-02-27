package com.cybersource.apisdk.connection.test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.apisdk.connection.HttpClientWrapper;
import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.SignatureGenerationException;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

public class HttpClientWrapperHttpTest {

	@Mock
	HttpClientWrapper httpClientWrapper;
	
	@Mock
	MerchantConfig merchantConfig;
	
	@Mock
	RequestContext requestContext;
	
	

	private Properties merchantProp;
	private String id = "5281957982016993203523";
	private String requestTarget = GlobalLabelParameters.REQUEST_TARGET;
	private String requestJsonPath = "src/test/resources/request.json";
	private String url = "https://";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void setHeaderDataTestForGet() throws ConfigException, IOException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext=new RequestContext(merchantConfig.getLogger());
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
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
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
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
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
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
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
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
	public void setHeaderDataTestForWrongMerchantId() throws ConfigException, IOException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("merchantID", "test");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext=new RequestContext(merchantConfig.getLogger());
		
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			requestContext.setRequestJsonPath(requestJsonPath);
			requestContext.setRequestMethodType("GET");
			requestTarget = requestTarget + id;
			requestContext.setRequestTarget(requestTarget);
			url = url + merchantConfig.getRequestHost() + requestTarget;
			requestContext.setUrl(url);
			
			httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig);
			Response resp = httpClientWrapper.sendRequest(requestContext);
			Assert.assertEquals("404", resp.getResponseCode());
	  }	
	}

	@Test
	public void openConnectionTestFailForUrl() throws ClientException, IOException, ConfigException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext=new RequestContext(merchantConfig.getLogger());

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			requestTarget = requestTarget + id;
			requestContext.setRequestTarget(requestTarget);
			requestContext.setUrl("abc");
			requestContext.setRequestJsonPath(requestJsonPath);
			requestContext.setRequestMethodType("GET");
			
			thrown.expect(ClientException.class);
			thrown.expectMessage(GlobalLabelParameters.URL_NOT_SET);
		   
			httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig);
			httpClientWrapper.sendRequest(requestContext);
		}
	}


	@Test
	public void setRequestMethodWrongTest() throws ClientException, IOException, ConfigException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			requestContext=new RequestContext(merchantConfig.getLogger());
			requestTarget = requestTarget + id;
			requestContext.setRequestTarget(requestTarget);
			url = url + merchantConfig.getRequestHost() + requestTarget;
			requestContext.setUrl(url);
			requestContext.setRequestJsonPath(requestJsonPath);
			requestContext.setRequestMethodType("option");
			
			thrown.expect(ClientException.class);
			thrown.expectMessage("HTTP Request method invalid. Supported types(POST, PUT, GET, DELETE, PATCH)");
			
			httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig);
			httpClientWrapper.sendRequest(requestContext);
		}
	}
	
	@Test
	public void getTestForGet() throws ClientException, IOException, ConfigException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			requestContext=new RequestContext(merchantConfig.getLogger());
			requestTarget = requestTarget + id;
			requestContext.setRequestTarget(requestTarget);
			requestContext.setRequestJsonPath(requestJsonPath);
			requestContext.setRequestMethodType("GET");
	
			url = url + merchantConfig.getRequestHost() + requestTarget;
			requestContext.setUrl(url);
	
			httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig);
			Response str = httpClientWrapper.sendRequest(requestContext);
			Assert.assertNotNull(str);
		}
	}

	@Test
	public void getTestForPost() throws ClientException, IOException, ConfigException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			requestContext=new RequestContext(merchantConfig.getLogger());
			requestContext.setRequestTarget(requestTarget);
			requestContext.setRequestJsonPath(requestJsonPath);
			requestContext.setRequestMethodType("POST");
	
			url = url + merchantConfig.getRequestHost() + requestTarget;
			requestContext.setUrl(url);
	
			httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig);
			Response str = httpClientWrapper.sendRequest(requestContext);
			Assert.assertNotNull(str);
	  }
	}

	@Test
	public void getTestForPut() throws IOException, ConfigException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			requestContext=new RequestContext(merchantConfig.getLogger());
			requestContext.setRequestTarget(requestTarget);
			requestContext.setRequestJsonPath(requestJsonPath);
			requestContext.setRequestMethodType("PUT");
	
			url = url + merchantConfig.getRequestHost() +requestTarget;
			requestContext.setUrl(url);
			
			httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig);
			Response str = httpClientWrapper.sendRequest(requestContext);
			Assert.assertNotNull(str);
		}

	}
	
	@Test
	public void getTestForPatch() throws IOException, ConfigException, ClientException, SignatureGenerationException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			requestContext=new RequestContext(merchantConfig.getLogger());
			requestContext.setRequestTarget(requestTarget);
			requestContext.setRequestJsonPath(requestJsonPath);
			requestContext.setRequestMethodType("PATCH");
	
			url = url + merchantConfig.getRequestHost() + requestTarget;
			requestContext.setUrl(url);
	
			httpClientWrapper = HttpClientWrapper.getInstance(merchantConfig);
			Response str = httpClientWrapper.sendRequest(requestContext);
			Assert.assertNotNull(str);
	   }
    }
	
}
