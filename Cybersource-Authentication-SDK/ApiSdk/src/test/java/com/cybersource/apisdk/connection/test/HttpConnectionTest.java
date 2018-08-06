package com.cybersource.apisdk.connection.test;

import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.apisdk.connection.HttpConnection;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

public class HttpConnectionTest {

	@Mock
	HttpConnection httpConnection;
	@Mock
	MerchantConfig merchantConfig;

	private Properties merchantProp;
	private String getID = "5281957982016993203523";
	private String requestTarget = GlobalLabelParameters.REQUEST_TARGET;
	private String requestJsonPath = "src/test/resources/request.json";

	@Mock
	Logger logger;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void setHeaderDataGetTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setRequestType("GET");
		requestTarget = requestTarget + getID;
		merchantConfig.setRequestTarget(requestTarget);
		logger = Log4j.getInstance(merchantConfig);

		httpConnection = new HttpConnection(merchantConfig, logger);
		httpConnection.setHeaderData(merchantConfig.getMerchantID());
		Assert.assertNotNull(merchantConfig);

	}

	@Test
	public void openConnectionTestFail() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setRequestType("GET");
		requestTarget = requestTarget + getID;
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setURL(null);
		logger = Log4j.getInstance(merchantConfig);

		httpConnection = new HttpConnection(merchantConfig, logger);
		httpConnection.openConnection();
		Assert.assertNull(merchantConfig.getURL());

	}

	@Test
	public void setJsonApplicationTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setRequestType("GET");
		requestTarget = requestTarget + getID;
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestJsonPath(requestJsonPath);
		logger = Log4j.getInstance(merchantConfig);

		httpConnection = new HttpConnection(merchantConfig, logger);
		httpConnection.setContentType();
		Assert.assertNotNull(merchantConfig);

	}

	@Test
	public void setSignatureTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setRequestType("GET");
		requestTarget = requestTarget + getID;
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestJsonPath(requestJsonPath);
		logger = Log4j.getInstance(merchantConfig);

		httpConnection = new HttpConnection(merchantConfig, logger);
		httpConnection.setSignature();
		Assert.assertNotNull(merchantConfig);

	}

	@Test
	public void setRequestMethodTest() throws Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setRequestType("GET");
		requestTarget = requestTarget + getID;
		merchantConfig.setRequestTarget(requestTarget);
		merchantConfig.setRequestJsonPath(requestJsonPath);
		logger = Log4j.getInstance(merchantConfig);

		httpConnection = new HttpConnection(merchantConfig, logger);
		httpConnection.setRequestMethod("GET");
		Assert.assertNotNull(merchantConfig.getRequestType());

	}

}
