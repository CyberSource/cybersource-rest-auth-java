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
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;

import junit.framework.Assert;

public class JwtAuthenticationStrategyTest {

	@Mock
	JWTAuthenticationStrategy jwtUrlConnection;

	@Mock
	MerchantConfig merchantConfig;
	@Mock
	MerchantUtil merchantUtil;

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
	public void getTestForGet() throws ClientException, IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		requestTarget = requestTarget + id;
		merchantConfig.setRequestTarget(requestTarget);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantUtil.setRequestType("GET");

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setUrl(url);

		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails();

		jwtUrlConnection = new JWTAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
		Response str = jwtUrlConnection.jwtConnection();
		Assert.assertNotNull(str);
	}

	@Test
	public void getTestForPost() throws ClientException, IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(requestTarget);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantUtil.setRequestType("POST");

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setUrl(url);

		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails();

		jwtUrlConnection = new JWTAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
	    Response resp = jwtUrlConnection.postDocument(merchantUtil.getRequestData());
		Assert.assertNotNull(resp);
	}

	@Test
	public void getTestForPut() throws IOException, ConfigException, ClientException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(requestTarget);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantUtil.setRequestType("PUT");

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setUrl(url);

		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails();

		jwtUrlConnection = new JWTAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
		response=jwtUrlConnection.postDocument(merchantUtil.getRequestData());
		Assert.assertNotNull(response);
		

	}
	
	@Test
	public void getTestForPatch() throws IOException, ConfigException, ClientException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestTarget(requestTarget);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantUtil.setRequestType("PATCH");

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setUrl(url);

		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails();

		jwtUrlConnection = new JWTAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
		response=jwtUrlConnection.executePatch(merchantUtil.getRequestData());
		Assert.assertNotNull(response);
	}


	@Test
	public void setRequestMethodTest() throws ClientException, IOException, ConfigException {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		requestTarget = requestTarget + id;
		merchantConfig.setRequestTarget(requestTarget);
		url = "https://" + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		 /*Set the URL.*/ 
		merchantConfig.setUrl(url);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestType("GET");
		merchantUtil.setRequestJsonPath(requestJsonPath);

		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails();

		jwtUrlConnection = new JWTAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
		jwtUrlConnection.openConnection();
		jwtUrlConnection.setRequestMethod("GET");
		Assert.assertNotNull(merchantUtil.getRequestType());

	}

	@Test
	public void openConnectionEmptyUrlTest() throws ClientException, IOException, ConfigException {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		requestTarget = requestTarget + id;
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setUrl("");
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantUtil.setRequestType("GET");
		
		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails();
		
		thrown.expect(ClientException.class);
		thrown.expectMessage(GlobalLabelParameters.URL_NOT_SET );
		
		
		jwtUrlConnection = new JWTAuthenticationStrategy(merchantConfig,merchantUtil.getRequestData(), logger);
		jwtUrlConnection.openConnection();

	}

}
