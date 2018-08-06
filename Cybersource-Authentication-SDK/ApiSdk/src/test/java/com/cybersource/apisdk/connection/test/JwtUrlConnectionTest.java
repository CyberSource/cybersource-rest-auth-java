package com.cybersource.apisdk.connection.test;

import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.apisdk.connection.JwtUrlConnection;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

import junit.framework.Assert;

public class JwtUrlConnectionTest {

	@Mock
	JwtUrlConnection jwtUrlConnection;

	@Mock
	MerchantConfig merchantConfig;

	private Properties merchantProp;
	private String get_ID = "5281957982016993203523";
	private String url = "http://";
	private String requestTarget = GlobalLabelParameters.REQUEST_TARGET;
	private String requestJsonPath = "src/test/resources/request.json";

	@Mock
	Logger logger;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void getTestForGet() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		requestTarget = requestTarget + get_ID;
		merchantConfig.setRequestTarget(requestTarget);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setURL(url);

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);

		jwtUrlConnection = new JwtUrlConnection(merchantConfig, logger);
		String str = jwtUrlConnection.get();
		Assert.assertNotNull(str);
	}

	@Test
	public void getTestForPost() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestJsonPath(requestJsonPath);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setURL(url);

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);

		jwtUrlConnection = new JwtUrlConnection(merchantConfig, logger);
		String str = jwtUrlConnection.postDocument(merchantConfig.getRequestData());
		Assert.assertNotNull(str);
	}

	@Test
	public void getTestForPut() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("PUT");
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestJsonPath(requestJsonPath);

		url = url + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
		merchantConfig.setURL(url);

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);

		thrown.expect(Exception.class);

		jwtUrlConnection = new JwtUrlConnection(merchantConfig, logger);
		jwtUrlConnection.postDocument(merchantConfig.getRequestData());

	}

	@Test
	public void setJsonApplicationTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		requestTarget = requestTarget + get_ID;
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestJsonPath(requestJsonPath);

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);

		jwtUrlConnection = new JwtUrlConnection(merchantConfig, logger);
		jwtUrlConnection.setContentType();
		Assert.assertNotNull(merchantConfig.getRequestJsonPath());

	}

	@Test
	public void setSignatureTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		requestTarget = requestTarget + get_ID;
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestJsonPath(requestJsonPath);

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);

		jwtUrlConnection = new JwtUrlConnection(merchantConfig, logger);
		jwtUrlConnection.setSignature();
		Assert.assertNotNull(merchantConfig.getRequestJsonPath());

	}

	@Test
	public void setRequestMethodTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		requestTarget = requestTarget + get_ID;
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestJsonPath(requestJsonPath);

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);

		jwtUrlConnection = new JwtUrlConnection(merchantConfig, logger);
		jwtUrlConnection.setRequestMethod("GET");
		Assert.assertNotNull(merchantConfig.getRequestType());

	}

	@Test
	public void openConnectionEmptyUrlTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		requestTarget = requestTarget + get_ID;
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestJsonPath(requestJsonPath);
		merchantConfig.setURL("");

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);

		jwtUrlConnection = new JwtUrlConnection(merchantConfig, logger);
		jwtUrlConnection.openConnection();
		Assert.assertNotNull(merchantConfig.getURL());

	}

}
