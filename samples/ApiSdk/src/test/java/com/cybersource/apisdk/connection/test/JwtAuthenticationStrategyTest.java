package com.cybersource.apisdk.connection.test;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.apisdk.connection.JWTAuthenticationStrategy;
import com.cybersource.apisdk.model.Response;
import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.AuthenticationRequestContext;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

import junit.framework.Assert;

public class JwtAuthenticationStrategyTest {

	@Mock
	JWTAuthenticationStrategy jwtUrlConnection;

	@Mock
	MerchantConfig merchantConfig;
	@Mock
	AuthenticationRequestContext merchantUtil;

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

//	@Test
//	public void getTestForGet() throws ClientException, IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestTarget = requestTarget + id;
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestTarget(requestTarget);
//		merchantUtil.setRequestJsonPath(requestJsonPath);
//		merchantUtil.setRequestType("GET");
//
//		url = url + merchantConfig.getRequestHost() + AuthenticationRequestContext.getRequestTarget();
//		merchantUtil.setUrl(url);
//
//		logger = merchantConfig.getLogger();
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
//			Assert.assertNull(null);
//		} else {
//			AuthenticationRequestContext.validateMerchantDetails();
//			jwtUrlConnection = new JWTAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
//			Response str = jwtUrlConnection.jwtConnection();
//			Assert.assertNotNull(str);
//		}	
//	}
//
//	@Test
//	public void getTestForPost() throws ClientException, IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestTarget(requestTarget);
//		merchantUtil.setRequestJsonPath(requestJsonPath);
//		merchantUtil.setRequestType("POST");
//
//		url = url + merchantConfig.getRequestHost() + AuthenticationRequestContext.getRequestTarget();
//		merchantUtil.setUrl(url);
//
//		logger = merchantConfig.getLogger();
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
//			Assert.assertNull(null);
//		} else {
//			AuthenticationRequestContext.validateMerchantDetails();
//			jwtUrlConnection = new JWTAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
//		    Response resp = jwtUrlConnection.postDocument(merchantUtil.getRequestData());
//			Assert.assertNotNull(resp);
//		}	
//	}
//
//	@Test
//	public void getTestForPut() throws IOException, ConfigException, ClientException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestTarget(requestTarget);
//		merchantUtil.setRequestJsonPath(requestJsonPath);
//		merchantUtil.setRequestType("PUT");
//
//		url = url + merchantConfig.getRequestHost() + AuthenticationRequestContext.getRequestTarget();
//		merchantUtil.setUrl(url);
//
//		logger = merchantConfig.getLogger();
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
//			Assert.assertNull(null);
//		} else {
//			AuthenticationRequestContext.validateMerchantDetails();
//			jwtUrlConnection = new JWTAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
//			response=jwtUrlConnection.postDocument(merchantUtil.getRequestData());
//			Assert.assertNotNull(response);
//		}
//
//	}
//	
//	@Test
//	public void getTestForPatch() throws IOException, ConfigException, ClientException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestTarget(requestTarget);
//		merchantUtil.setRequestJsonPath(requestJsonPath);
//		merchantUtil.setRequestType("PATCH");
//
//		url = url + merchantConfig.getRequestHost() + AuthenticationRequestContext.getRequestTarget();
//		merchantUtil.setUrl(url);
//
//		logger = merchantConfig.getLogger();
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
//			Assert.assertNull(null);
//		} else {
//			AuthenticationRequestContext.validateMerchantDetails();
//			jwtUrlConnection = new JWTAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
//			response=jwtUrlConnection.executePatch(merchantUtil.getRequestData());
//			Assert.assertNotNull(response);
//		}	
//	}
//
//
//	@Test
//	public void setRequestMethodTest() throws ClientException, IOException, ConfigException {
//
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestTarget = requestTarget + id;
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestTarget(requestTarget);
//		url = "https://" + merchantConfig.getRequestHost() + AuthenticationRequestContext.getRequestTarget();
//		 /*Set the URL.*/ 
//		merchantUtil.setUrl(url);
//		merchantUtil.setRequestType("GET");
//		merchantUtil.setRequestJsonPath(requestJsonPath);
//
//		logger = merchantConfig.getLogger();
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
//			Assert.assertNull(null);
//		} else {
//			AuthenticationRequestContext.validateMerchantDetails();
//			jwtUrlConnection = new JWTAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
//			jwtUrlConnection.openConnection();
//			jwtUrlConnection.setRequestMethod("GET");
//			Assert.assertNotNull(AuthenticationRequestContext.getRequestType());
//		}	
//
//	}
//
//	@Test
//	public void openConnectionEmptyUrlTest() throws ClientException, IOException, ConfigException {
//
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestTarget = requestTarget + id;
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestTarget(requestTarget);
//		merchantUtil.setUrl("");
//		merchantUtil.setRequestJsonPath(requestJsonPath);
//		merchantUtil.setRequestType("GET");
//		
//		logger = merchantConfig.getLogger();
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
//			Assert.assertNull(null);
//		} else {
//			AuthenticationRequestContext.validateMerchantDetails();
//			thrown.expect(ClientException.class);
//			thrown.expectMessage(GlobalLabelParameters.URL_NOT_SET );
//			jwtUrlConnection = new JWTAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
//			jwtUrlConnection.openConnection();
//		}
//		
//		
//
//	}

}
