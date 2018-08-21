package com.cybersource.authsdk.core.test;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.PropertiesUtil;

public class MerchantConfigTest {

	@Mock
	MerchantConfig merchantConfig;

	@Mock
	Logger logger;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Properties merchantProp;
	private String requestJsonPath = "src/test/resources/request.json";
	private String jsonPathforCheck = "src/test/resources/abc.json";
	private String testJsonPath = "src/test/resources/test.json";

	// Test case for KeyAlias Validation
	@Test
	public void getKeyAliasTestForEmpty() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		String keyAlias = merchantConfig.getKeyAlias();
		Assert.assertNotNull(keyAlias);
	}

	@Test
	public void getKeyAliasTestPass() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		String keyAlias = merchantConfig.getKeyAlias();
		Assert.assertNotNull(keyAlias);
	}

	@Test
	public void getRequestDataTestPass() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestJsonPath(requestJsonPath);

		String requestData = merchantConfig.getRequestData();
		Assert.assertNotNull(requestData);
	}

	// test cases for validate merchant detail
	@Test
	public void validateMerchantDetailsHttpTestforGet() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setAuthenticationType("Http_Signature");
		merchantConfig.setRequestType("GET");
		this.logger = Log4j.getInstance(merchantConfig);

		boolean check = merchantConfig.validateMerchantDetails(logger);
		Assert.assertEquals(true, check);

	}

	@Test
	public void validateMerchantDetailsHttpTestforPost() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setAuthenticationType("Http_Signature");
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestJsonPath(requestJsonPath);
		this.logger = Log4j.getInstance(merchantConfig);

		boolean check = merchantConfig.validateMerchantDetails(logger);
		Assert.assertEquals(true, check);

	}

	@Test
	public void validateMerchantDetailsHttpTestforPut() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setAuthenticationType("Http_Signature");
		merchantConfig.setRequestType("PUT");
		merchantConfig.setRequestJsonPath(requestJsonPath);
		this.logger = Log4j.getInstance(merchantConfig);

		boolean check = merchantConfig.validateMerchantDetails(logger);
		Assert.assertEquals(true, check);

	}

	@Test
	public void validateMerchantDetailsJwtTestForGet() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setAuthenticationType("jwt");
		merchantConfig.setRequestType("GET");
		this.logger = Log4j.getInstance(merchantConfig);

		boolean check = merchantConfig.validateMerchantDetails(logger);
		Assert.assertEquals(true, check);

	}

	@Test
	public void validateMerchantDetailsJwtTestForPost() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setAuthenticationType("jwt");
		merchantConfig.setRequestType("POST");
		merchantConfig.setRequestJsonPath(requestJsonPath);
		this.logger = Log4j.getInstance(merchantConfig);

		boolean check = merchantConfig.validateMerchantDetails(logger);
		Assert.assertEquals(true, check);

	}

	@Test
	public void validateMerchantDetailsJwtTestForPut() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		merchantConfig.setAuthenticationType("jwt");
		merchantConfig.setRequestType("PUT");
		merchantConfig.setRequestJsonPath(requestJsonPath);
		this.logger = Log4j.getInstance(merchantConfig);

		boolean check = merchantConfig.validateMerchantDetails(logger);
		Assert.assertEquals(true, check);

	}

	//// Test cases for keyPassword check

	@Test
	public void checkKeyPasswordTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		boolean check = merchantConfig.checkKeyPassword();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkKeyPasswordNullTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setKeyPass(null);

		boolean check = merchantConfig.checkKeyPassword();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkKeyPasswordEmptyTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setKeyPass("");

		boolean check = merchantConfig.checkKeyPassword();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkKeyPasswordNotEqualMerchantIdTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setKeyPass("test");

		boolean check = merchantConfig.checkKeyPassword();
		Assert.assertEquals(true, check);

	}

	// Test cases for run environment check

	@Test
	public void checkRunEvironmentTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.checkRunEvironment();
		Assert.assertEquals(true, merchantConfig.checkRunEvironment());

	}

	@Test
	public void checkRunEvironmentForNullTest() throws Exception {

		merchantConfig = new MerchantConfig();
		merchantConfig.setRunEnvironment(null);
		merchantConfig.checkRunEvironment();
		Assert.assertEquals(false, merchantConfig.checkRunEvironment());

	}

	@Test
	public void checkRunEvironmentForEmptyTest() throws IOException, ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setRunEnvironment("");

		merchantConfig.checkRunEvironment();
		Assert.assertEquals(false, merchantConfig.checkRunEvironment());

	}

	@Test
	public void checkRunEvironmentForProductionTest() throws IOException, ConfigException {

		merchantConfig = new MerchantConfig();
		merchantConfig.setRunEnvironment("CyberSource.Environment.PRODUCTION");

		merchantConfig.checkRunEvironment();
		Assert.assertEquals(true, merchantConfig.checkRunEvironment());

	}

	@Test
	public void checkRunEvironmentTestFail() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRunEnvironment("test");

		merchantConfig.checkRunEvironment();
		Assert.assertEquals(true, merchantConfig.checkRunEvironment());

	}

	// Test cases for check merchant keyId

	@Test
	public void checkMerchantKeyIdTestForNull() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setMerchantKeyId(null);

		merchantConfig.checkMerchantKeyId();
		Assert.assertEquals(false, merchantConfig.checkMerchantKeyId());

	}

	@Test
	public void checkMerchantKeyIdTestForEmpty() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setMerchantKeyId("");

		merchantConfig.checkMerchantKeyId();
		Assert.assertEquals(false, merchantConfig.checkMerchantKeyId());

	}

	// Test cases for check merchantSecretKey

	@Test
	public void checkMerchantSecertKeyTestForNull() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setMerchantsecretKey(null);

		boolean check = merchantConfig.checkMerchantSecertKey();
		Assert.assertEquals(false, check);

	}

	@Test
	public void checkMerchantSecertKeyTestForEmpty() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setMerchantsecretKey("");

		boolean check = merchantConfig.checkMerchantSecertKey();
		Assert.assertEquals(false, check);

	}

	// Test cases for log max size and folder check

	@Test
	public void checkLogMaxSizeAndFolderTest() throws IOException, Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		boolean check = merchantConfig.checkLogMaxSizeAndFolder();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkLogMaxSizeAndFolderTestForNull() throws IOException, Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setLogDirectory(null);

		boolean check = merchantConfig.checkLogMaxSizeAndFolder();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkLogMaxSizeTestForEmpty() throws IOException, Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setLogDirectory("");

		boolean check = merchantConfig.checkLogMaxSizeAndFolder();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkLogMaxSizeTestFail() throws IOException, Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setLogDirectory(null);

		boolean check = merchantConfig.checkLogMaxSizeAndFolder();
		Assert.assertEquals(true, check);

	}

	// Test cases for json path check

	@Test
	public void checkJsonPathTest() throws IOException, Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setIsRequestType(true);

		boolean check = merchantConfig.checkJsonPath(requestJsonPath);
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkJsonPathTestWrongRequestType() throws IOException, Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("OPTION");
		merchantConfig.setIsRequestType(true);

		boolean check = merchantConfig.checkJsonPath(jsonPathforCheck);
		Assert.assertEquals(false, check);

	}

	@Test
	public void checkJsonPathNullTest() throws IOException, Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		merchantConfig.setIsRequestType(true);

		boolean check = merchantConfig.checkJsonPath(null);
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkJsonPathEmptyTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		merchantConfig.setIsRequestType(true);

		boolean check = merchantConfig.checkJsonPath("");
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkJsonPathTestFileNotExist() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setIsRequestType(true);

		boolean check = merchantConfig.checkJsonPath(jsonPathforCheck);
		Assert.assertEquals(false, check);

	}

	@Test
	public void checkJsonPathTestIsrequestTypeFalse() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setIsRequestType(false);

		boolean check = merchantConfig.checkJsonPath(jsonPathforCheck);
		Assert.assertEquals(false, check);

	}
	// test cases for keyalias check

	@Test
	public void checkKeyAliasTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setKeyAlias("testrest");

		boolean check = merchantConfig.checkKeyAlias();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkKeyAliasTestForNull() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setKeyAlias(null);

		boolean check = merchantConfig.checkKeyAlias();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkKeyAliasTestForEmpty() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setKeyAlias("");

		boolean check = merchantConfig.checkKeyAlias();
		Assert.assertEquals(true, check);
	}

	@Test
	public void checkKeyAliasTestForWrong() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setKeyAlias("test");

		boolean check = merchantConfig.checkKeyAlias();
		Assert.assertEquals(true, check);

	}

	// test case for merchantDetails

	@Test
	public void setMerchantDetailsTestForNull() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setMerchantID(null);

		merchantConfig.setMerchantDetails();
		Assert.assertEquals(false, MerchantConfig.isSetMerchantDetails);

	}

	@Test
	public void setMerchantDetailsTestForEmpty() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setMerchantID("");

		merchantConfig.setMerchantDetails();
		Assert.assertEquals(false, MerchantConfig.isSetMerchantDetails);

	}

	@Test
	public void setMerchantDetailsFail() throws NullPointerException, Exception {
		merchantProp = null;
		merchantConfig = new MerchantConfig(merchantProp);

		thrown.expect(NullPointerException.class);
		merchantConfig.setMerchantDetails();

	}

	// test cases for check key file
	@Test
	public void CheckKeyFileTestForNull() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setKeyFilename(null);

		boolean check = merchantConfig.checkKeyFile();
		Assert.assertEquals(true, check);

	}

	@Test
	public void CheckKeyFileTestForKeyFileNotExist() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setKeyFilename("test");

		boolean check = merchantConfig.checkKeyFile();
		Assert.assertEquals(false, check);

	}

	@Test
	public void CheckKeyFileTestForFail() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setKeyFilename("test");
		merchantConfig.setKeysDirectory(null);

		boolean check = merchantConfig.checkKeyFile();
		Assert.assertEquals(false, check);

	}

	// Test cases for request data

	@Test
	public void getRequestDataTestFail() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");

		merchantConfig.setRequestJsonPath(testJsonPath);
		String Data = merchantConfig.getRequestData();
		Assert.assertNotNull(Data);

	}

	// Test cases for check request type

	@Test
	public void checkRequestTypeTestForGet() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");

		boolean check = merchantConfig.checkRequestType();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkRequestTypeTestForPost() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");

		boolean check = merchantConfig.checkRequestType();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkRequestTypeTestForPut() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("PUT");

		boolean check = merchantConfig.checkRequestType();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkRequestTypeTestForDelete() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("Delete");

		boolean check = merchantConfig.checkRequestType();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkRequestTypeTestFail() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setRequestType("option");

		boolean check = merchantConfig.checkRequestType();
		Assert.assertEquals(false, check);
	}

	@Test
	public void setProxyAddressTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setProxyAddress("userproxy.visa.com");
		String str = merchantConfig.getProxyAddress();
		Assert.assertNotNull(str);
	}

	@Test
	public void isRequestJsonTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		boolean str = merchantConfig.isRequestJson();
		Assert.assertNotNull(str);
	}

	@Test
	public void setRequestDataTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setRequestData("requestData");
		String str = merchantConfig.getRequestData();
		Assert.assertNotNull(str);
	}

	@Test
	public void setV_c_CorelationIDTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setV_c_CorelationID("test");
		String str = merchantConfig.getV_c_CorelationID();
		Assert.assertNotNull(str);
	}

	@Test
	public void setResponseCodeTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setResponseCode("200");
		String str = merchantConfig.getResponseCode();
		Assert.assertNotNull(str);
	}

	@Test
	public void setResponseMessageTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setResponseMessage("success");
		String str = merchantConfig.getResponseMessage();
		Assert.assertNotNull(str);
	}

	@Test
	public void setUrlRequestHeaderTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setUrlRequestHeader("requestHeader");
		String str = merchantConfig.getUrlRequestHeader();
		Assert.assertNotNull(str);
	}

	@Test
	public void setKeyAliasTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setKeyAlias("testrest");
		String str = merchantConfig.getKeyAlias();
		Assert.assertNotNull(str);
	}

	@Test
	public void setMerchantIDTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setMerchantID("testrest");
		String str = merchantConfig.getMerchantID();
		Assert.assertNotNull(str);
	}

	@Test
	public void setPropsTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setProps(merchantProp);
		Properties str = merchantConfig.getProps();
		Assert.assertNotNull(str);
	}

	@Test
	public void setURLTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setURL("testrest");
		String str = merchantConfig.getURL();
		Assert.assertNotNull(str);
	}

	@Test
	public void setEnableLogTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setEnableLog(true);
		boolean str = merchantConfig.getEnableLog();
		Assert.assertNotNull(str);
	}

	@Test
	public void setGetIDTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setGetID("1234");
		String str = merchantConfig.getGetID();
		Assert.assertNotNull(str);
	}

	@Test
	public void setDateTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setDate("11/12/1998");
		String str = merchantConfig.getDate();
		Assert.assertNotNull(str);
	}

	@Test
	public void setKeysDirectoryTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setKeysDirectory("resource");
		String str = merchantConfig.getKeysDirectory();
		Assert.assertNotNull(str);
	}

	@Test
	public void getKeyTypeTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		String str = merchantConfig.getKeyType();
		Assert.assertNotNull(str);
	}

	@Test
	public void setAuthenticationTypeTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setAuthenticationType("jwt");
		String str = merchantConfig.getAuthenticationType();
		Assert.assertNotNull(str);
	}

	@Test
	public void setAuthenticationTypeTestForNull() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setAuthenticationType("HTTP_Signature");
		String str = merchantConfig.getAuthenticationType();
		Assert.assertNotNull(str);
	}

	@Test
	public void setSendToProductionTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setSendToProduction(true);
		boolean str = merchantConfig.getSendToProduction();
		Assert.assertNotNull(str);
	}

	@Test
	public void setKeyFilenameTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setKeyFilename("testrest");
		String str = merchantConfig.getKeyFilename();
		Assert.assertNotNull(str);
	}

	@Test
	public void setRequestTargetTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setRequestTarget("/pts/v2/payments");
		String str = merchantConfig.getRequestTarget();
		Assert.assertNotNull(str);
	}

	@Test
	public void setPasswordTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setPassword("testrest");
		String str = merchantConfig.getPassword();
		Assert.assertNotNull(str);
	}

	@Test
	public void setKeyTypeTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setKeyType("testrest");
		String str = merchantConfig.getKeyType();
		Assert.assertNotNull(str);
	}

	@Test
	public void setTimeoutTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setTimeout(10);
		int time = merchantConfig.getTimeout();
		Assert.assertNotNull(time);
	}

	@Test
	public void setProxyUserTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setProxyUser("testrest");
		String str = merchantConfig.getProxyUser();
		Assert.assertNotNull(str);
	}

	@Test
	public void setProxyPasswordTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setProxyPassword("testrest");
		String str = merchantConfig.getProxyPassword();
		Assert.assertNotNull(str);
	}

	@Test
	public void setProxyPasswordTestForNull() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setProxyPassword(null);
		String str = merchantConfig.getProxyPassword();
		Assert.assertNotNull(str);
	}

	@Test
	public void setEffectivePasswordTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setEffectivePassword("testrest");
		String str = merchantConfig.getEffectivePassword();
		Assert.assertNotNull(str);
	}

	@Test
	public void setRequestHostTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setRequestHost("apitest.cybersource,com");
		String str = merchantConfig.getRequestHost();
		Assert.assertNotNull(str);
	}

	@Test
	public void setLogSignedDataTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setLogSignedData(true);
		boolean str = merchantConfig.getLogSignedData();
		Assert.assertNotNull(str);
	}

	@Test
	public void setLogMaximumSizeTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setLogMaximumSize("5k");
		String str = merchantConfig.getLogMaximumSize();
		Assert.assertNotNull(str);
	}

	@Test
	public void setProxyPortTest() throws ConfigException {
		MerchantConfig merchantConfig = new MerchantConfig();
		merchantConfig.setProxyPort(4040);
		int port = merchantConfig.getProxyPort();
		Assert.assertNotNull(port);
	}

	@Test
	public void setLogFileNameTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setLogFilename("cybs");
		String str = merchantConfig.getLogFilename();
		Assert.assertNotNull(str);
	}

	@Test
	public void setLogDirectoryTest() throws ConfigException {
		MerchantConfig merchantConfig = new MerchantConfig();
		merchantConfig.setLogDirectory("log");
		String str = merchantConfig.getLogDirectory();
		Assert.assertNotNull(str);
	}

	@Test
	public void setRequestJsonTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setRequestJson(true);
		boolean str = merchantConfig.getrequestJson();
		Assert.assertNotNull(str);
	}

	// Test cases for Log directory
	@Test
	public void getLogFileTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setRequestJson(true);
		merchantConfig.setLogDirectory("log");

		boolean check = merchantConfig.getLogFile();
		Assert.assertEquals(true, check);
	}

	@Test
	public void getLogFileTestFail() throws Exception {
		merchantConfig = new MerchantConfig();
		merchantConfig.setRequestJson(true);
		merchantConfig.setLogDirectory("test");

		boolean check = merchantConfig.getLogFile();
		Assert.assertEquals(true, check);

	}

	// test cases for get Boolean property

	@Test
	public void getBooleanPropertyTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		String prop = "authenticationType";
		thrown.expect(ConfigException.class);
		thrown.expectMessage(prop + " has an invalid value.");

		merchantConfig.getBooleanProperty(merchantConfig.getMerchantID(), prop, true);

	}

	// test cases for get Integer property

	@Test
	public void getIntegerPropertyTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		String prop = "authenticationType";
		thrown.expect(ConfigException.class);
		thrown.expectMessage(prop + " has an invalid value.");

		merchantConfig.getIntegerProperty(merchantConfig.getMerchantID(), prop, 1);

	}
}
