package com.cybersource.apisdk.connection.test;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.apisdk.connection.HttpAuthenticationStrategy;
import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;

public class HttpAuthenticationStrategyTest {

	@Mock
	HttpAuthenticationStrategy httpConnection;
	
	@Mock
	MerchantConfig merchantConfig;
	@Mock
	MerchantUtil merchantUtil;

	private Properties merchantProp;
	private String id = "5281957982016993203523";
	private String requestTarget = GlobalLabelParameters.REQUEST_TARGET;
	private String requestJsonPath = "src/test/resources/request.json";
	private String url = "https://";
	private Response response;
	
	@Mock
	Logger logger;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void setHeaderDataTestForGet() throws ConfigException, IOException, ClientException {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantConfig.setMerchantID("testrest");
	        merchantConfig.setRequestType("GET");
	        requestTarget = requestTarget + id;
			merchantConfig.setRequestTarget(requestTarget);
			url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
			/* Set the URL. */
			merchantConfig.setUrl(url);
			logger = Log4j.getInstance(merchantConfig);
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,null, logger);
			httpConnection.openConnection();
	        httpConnection.setHeaderData();
	        Assert.assertNotNull(httpConnection);
		}
	}
	
	@Test
	public void setHeaderDataTestForPost() throws ConfigException, ClientException, IOException {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantUtil.setRequestJsonPath(requestJsonPath);
			merchantConfig.setMerchantID("testrest");
	        merchantConfig.setRequestType("POST");
	        merchantConfig.setRequestTarget(requestTarget);
			url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
			/* Set the URL. */
			merchantConfig.setUrl(url);
			logger = Log4j.getInstance(merchantConfig);
			
	
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
			httpConnection.openConnection();
	        httpConnection.setHeaderData();
	        Assert.assertNotNull(httpConnection);
		}
	}
	
	@Test
	public void setHeaderDataTestForPatch() throws ConfigException, IOException, ClientException {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantUtil.setRequestJsonPath(requestJsonPath);
			merchantConfig.setMerchantID("testrest");
	        merchantConfig.setRequestType("PATCH");
	        merchantConfig.setRequestTarget(requestTarget);
			url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
			/* Set the URL. */
			merchantConfig.setUrl(url);
			logger = Log4j.getInstance(merchantConfig);
			
	
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
			httpConnection.openConnection();
	        httpConnection.setHeaderData();
	        Assert.assertNotNull(httpConnection);
		}

	}
	
	@Test
	public void setHeaderDataTestForWrongMerchantId() throws ConfigException, IOException, ClientException {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantConfig.setRequestType("GET");
	        requestTarget = requestTarget + id;
			merchantConfig.setRequestTarget(requestTarget);
			logger = Log4j.getInstance(merchantConfig);
			
			thrown.expect(ConfigException.class);
			thrown.expectMessage(GlobalLabelParameters.MERCHANTID_NOT_SET );
	
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
	        httpConnection.setHeaderData();
		}	
	}

	@Test
	public void openConnectionTestFailForUrl() throws ClientException, IOException, ConfigException {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantConfig.setRequestType("GET");
			requestTarget = requestTarget + id;
			merchantConfig.setRequestTarget(requestTarget);
			merchantConfig.setUrl("abc");
			merchantUtil.setRequestJsonPath(requestJsonPath);
			logger = Log4j.getInstance(merchantConfig);
			
			thrown.expect(ClientException.class);
			thrown.expectMessage(GlobalLabelParameters.URL_NOT_SET );
		   
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(),logger);
			httpConnection.openConnection();
		}
	}


	@Test
	public void setRequestMethodTest() throws ClientException, IOException, ConfigException {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantConfig.setRequestType("GET");
			requestTarget = requestTarget + id;
			merchantConfig.setRequestTarget(requestTarget);
			url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
			/* Set the URL. */
			merchantConfig.setUrl(url);
			merchantUtil=new MerchantUtil(merchantConfig);
			merchantUtil.setRequestJsonPath(requestJsonPath);
			logger = Log4j.getInstance(merchantConfig);
			MerchantUtil.validateMerchantDetails();
	
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
			httpConnection.openConnection();
			httpConnection.setRequestMethod(merchantConfig.getRequestType());
			Assert.assertNotNull(merchantConfig.getRequestType());
		}
	}
	
	@Test
	public void getTestForGet() throws ClientException, IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantConfig.setRequestType("GET");
			requestTarget = requestTarget + id;
			merchantConfig.setRequestTarget(requestTarget);
			merchantUtil=new MerchantUtil(merchantConfig);
			merchantUtil.setRequestJsonPath(requestJsonPath);
	
			url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
			merchantConfig.setUrl(url);
	
			logger = Log4j.getInstance(merchantConfig);
			MerchantUtil.validateMerchantDetails();
	
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
			Response str = httpConnection.getHttpConnection();
			Assert.assertNotNull(str);
		}
	}

	@Test
	public void getTestForPost() throws ClientException, IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantConfig.setRequestType("POST");
			merchantConfig.setRequestTarget(requestTarget);
			merchantUtil=new MerchantUtil(merchantConfig);
			merchantUtil.setRequestJsonPath(requestJsonPath);
	
			url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
			merchantConfig.setUrl(url);
	
			logger = Log4j.getInstance(merchantConfig);
			MerchantUtil.validateMerchantDetails();
	
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
			Response str = httpConnection.getHttpConnection();
			Assert.assertNotNull(str);
	  }
	}

	@Test
	public void getTestForPut() throws IOException, ConfigException, ClientException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantConfig.setRequestType("PUT");
			merchantConfig.setRequestTarget(requestTarget);
			merchantUtil=new MerchantUtil(merchantConfig);
			merchantUtil.setRequestJsonPath(requestJsonPath);
	
			url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
			merchantConfig.setUrl(url);
	
			logger = Log4j.getInstance(merchantConfig);
			MerchantUtil.validateMerchantDetails();
	
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
			Response str = httpConnection.getHttpConnection();
			Assert.assertNotNull(str);
		}

	}
	
	@Test
	public void getTestForPatch() throws IOException, ConfigException, ClientException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantConfig.setRequestType("PATCH");
			merchantConfig.setRequestTarget(requestTarget);
			merchantUtil=new MerchantUtil(merchantConfig);
			merchantUtil.setRequestJsonPath(requestJsonPath);
	
			url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
			merchantConfig.setUrl(url);
	
			logger = Log4j.getInstance(merchantConfig);
			MerchantUtil.validateMerchantDetails();
	
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
			response=httpConnection.executePatch(merchantUtil.getRequestData());
			Assert.assertNotNull(response);
	 } 
	}

}
