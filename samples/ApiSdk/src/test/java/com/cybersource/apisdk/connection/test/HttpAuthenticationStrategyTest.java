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
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		merchantUtil=new MerchantUtil(merchantConfig);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantConfig.setMerchantID("testrest");
	        requestTarget = requestTarget + id;
	        merchantUtil.setRequestTarget(requestTarget);
			url = url + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
			/* Set the URL. */
			merchantUtil.setUrl(url);
			merchantUtil.setRequestType("GET");
			merchantUtil.setRequestJsonPath(requestJsonPath);
			logger = merchantConfig.getLogger();
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,null, logger);
			httpConnection.openConnection();
	        httpConnection.setHeaderData();
	        Assert.assertNotNull(httpConnection);
		}
	}
	
	@Test
	public void setHeaderDataTestForPost() throws ConfigException, ClientException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		merchantUtil=new MerchantUtil(merchantConfig);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			
			merchantConfig.setMerchantID("testrest");
	        merchantUtil.setRequestTarget(requestTarget);
			url = url + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
			/* Set the URL. */
			merchantUtil.setUrl(url);
			merchantUtil.setRequestJsonPath(requestJsonPath);
			merchantUtil.setRequestType("POST");
			logger = merchantConfig.getLogger();
			
	
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
			httpConnection.openConnection();
	        httpConnection.setHeaderData();
	        Assert.assertNotNull(httpConnection);
		}
	}
	
	@Test
	public void setHeaderDataTestForPatch() throws ConfigException, IOException, ClientException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		merchantUtil=new MerchantUtil(merchantConfig);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantConfig.setMerchantID("testrest");
	        merchantUtil.setRequestTarget(requestTarget);
			url = url + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
			/* Set the URL. */
			merchantUtil.setUrl(url);
			merchantUtil.setRequestJsonPath(requestJsonPath);
     		merchantUtil.setRequestType("PATCH");
			logger = merchantConfig.getLogger();
			
	
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
			httpConnection.openConnection();
	        httpConnection.setHeaderData();
	        Assert.assertNotNull(httpConnection);
		}

	}
	
	@Test
	public void setHeaderDataTestForWrongMerchantId() throws ConfigException, IOException, ClientException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		merchantConfig.setMerchantID(null);
		merchantUtil=new MerchantUtil(merchantConfig);
		
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantUtil.setRequestJsonPath(requestJsonPath);
			merchantUtil.setRequestType("GET");
			requestTarget = requestTarget + id;
			merchantUtil.setRequestTarget(requestTarget);
			url = url + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
			merchantUtil.setUrl(url);
			
			logger = merchantConfig.getLogger();
			
			thrown.expect(ConfigException.class);
			thrown.expectMessage(GlobalLabelParameters.MERCHANTID_NOT_SET );
	
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
			httpConnection.openConnection();
			httpConnection.setRequestMethod(MerchantUtil.getRequestType());
	        httpConnection.setHeaderData();
	    }	
	}

	@Test
	public void openConnectionTestFailForUrl() throws ClientException, IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		merchantUtil=new MerchantUtil(merchantConfig);

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			requestTarget = requestTarget + id;
			merchantUtil.setRequestTarget(requestTarget);
			merchantUtil.setUrl("abc");
			merchantUtil.setRequestJsonPath(requestJsonPath);
			merchantUtil.setRequestType("GET");
			logger = merchantConfig.getLogger();
			
			thrown.expect(ClientException.class);
			thrown.expectMessage(GlobalLabelParameters.URL_NOT_SET );
		   
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(),logger);
			httpConnection.openConnection();
		}
	}


	@Test
	public void setRequestMethodTest() throws ClientException, IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantUtil=new MerchantUtil(merchantConfig);
			requestTarget = requestTarget + id;
			merchantUtil.setRequestTarget(requestTarget);
			url = url + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
			/* Set the URL. */
			merchantUtil.setUrl(url);
			merchantUtil.setRequestJsonPath(requestJsonPath);
			merchantUtil.setRequestType("GET");
			logger = merchantConfig.getLogger();
			MerchantUtil.validateMerchantDetails();
	        
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
			httpConnection.openConnection();
			httpConnection.setRequestMethod(MerchantUtil.getRequestType());
		}
	}
	
	@Test
	public void getTestForGet() throws ClientException, IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantUtil=new MerchantUtil(merchantConfig);
			requestTarget = requestTarget + id;
			merchantUtil.setRequestTarget(requestTarget);
			merchantUtil.setRequestJsonPath(requestJsonPath);
			merchantUtil.setRequestType("GET");
	
			url = url + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
			merchantUtil.setUrl(url);
	
			logger = merchantConfig.getLogger();
			MerchantUtil.validateMerchantDetails();
	
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
			Response str = httpConnection.getHttpConnection();
			Assert.assertNotNull(str);
		}
	}

	@Test
	public void getTestForPost() throws ClientException, IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantUtil=new MerchantUtil(merchantConfig);
			merchantUtil.setRequestTarget(requestTarget);
			merchantUtil.setRequestJsonPath(requestJsonPath);
			merchantUtil.setRequestType("POST");
	
			url = url + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
			merchantUtil.setUrl(url);
	
			logger = merchantConfig.getLogger();
			MerchantUtil.validateMerchantDetails();
	
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
			Response str = httpConnection.getHttpConnection();
			Assert.assertNotNull(str);
	  }
	}

	@Test
	public void getTestForPut() throws IOException, ConfigException, ClientException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantUtil=new MerchantUtil(merchantConfig);
			merchantUtil.setRequestTarget(requestTarget);
			merchantUtil.setRequestJsonPath(requestJsonPath);
			merchantUtil.setRequestType("PUT");
	
			url = url + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
			merchantUtil.setUrl(url);
	
			logger = merchantConfig.getLogger();
			MerchantUtil.validateMerchantDetails();
	
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
			Response str = httpConnection.getHttpConnection();
			Assert.assertNotNull(str);
		}

	}
	
	@Test
	public void getTestForPatch() throws IOException, ConfigException, ClientException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertNull(null);
		} else {
			merchantUtil=new MerchantUtil(merchantConfig);
			merchantUtil.setRequestTarget(requestTarget);
			merchantUtil.setRequestJsonPath(requestJsonPath);
			merchantUtil.setRequestType("PATCH");
	
			url = url + merchantConfig.getRequestHost() + MerchantUtil.getRequestTarget();
			merchantUtil.setUrl(url);
	
			logger = merchantConfig.getLogger();
			MerchantUtil.validateMerchantDetails();
	
			httpConnection = new HttpAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
			response=httpConnection.executePatch(merchantUtil.getRequestData());
			Assert.assertNotNull(response);
	 } 
	}

}
