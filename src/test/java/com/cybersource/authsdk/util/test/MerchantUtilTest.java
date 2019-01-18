package com.cybersource.authsdk.util.test;

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
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;

public class MerchantUtilTest {
	
	@Mock
	MerchantConfig merchantConfig;
	
	@Mock
	MerchantUtil merchantUtil;

	@Mock
	Logger logger;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Properties merchantProp;
	private String requestJsonPath = "src/test/resources/request.json";
	private String jsonPathforCheck = "src/test/resources/abc.json";
	private String testJsonPath = "src/test/resources/test.json";
	
	// Test cases for request data
	@Test
	public void getRequestDataTestPass() throws ConfigException, IOException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil = new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);

		String requestData = merchantUtil.getRequestData();
		Assert.assertNotNull(requestData);
	}

	@Test
	public void getRequestDataTestFail() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantUtil = new MerchantUtil(merchantConfig);

		merchantUtil.setRequestJsonPath(testJsonPath);
		String Data = merchantUtil.getRequestData();
		Assert.assertNotNull(Data);

	}

	// test cases for validate merchant detail
	@Test
	public void validateMerchantDetailsHttpTestforGet() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantConfig.setAuthenticationType("Http_Signature");
		merchantConfig.setRequestType("GET");
		merchantConfig.setMerchantDetails();
		this.logger = Log4j.getInstance(merchantConfig);
		
		boolean check = false;
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Assert.assertEquals(false,check);
		} else {
			check = MerchantUtil.validateMerchantDetails();
			Assert.assertEquals(true,check);
		}
	}

	@Test
	public void validateMerchantDetailsHttpTestforPost() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantConfig.setAuthenticationType("Http_Signature");
		merchantConfig.setRequestType("POST");
		merchantConfig.setMerchantDetails();
		merchantUtil.setRequestJsonPath(requestJsonPath);
		this.logger = Log4j.getInstance(merchantConfig);

		boolean check = MerchantUtil.validateMerchantDetails();
		Assert.assertEquals(true, check);

	}

	@Test
	public void validateMerchantDetailsHttpTestforPut() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		
		merchantConfig.setAuthenticationType("Http_Signature");
		merchantConfig.setRequestType("PUT");
		merchantConfig.setMerchantDetails();
		merchantUtil=new MerchantUtil(merchantConfig);
		
		merchantUtil.setRequestJsonPath(requestJsonPath);
		this.logger = Log4j.getInstance(merchantConfig);

		boolean check = MerchantUtil.validateMerchantDetails();
		Assert.assertEquals(true, check);

	}

	@Test
	public void validateMerchantDetailsJwtTestForGet() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setAuthenticationType("jwt");
		merchantConfig.setRequestType("GET");
		merchantConfig.setMerchantDetails();
		merchantUtil=new MerchantUtil(merchantConfig);

		this.logger = Log4j.getInstance(merchantConfig);

		boolean check = MerchantUtil.validateMerchantDetails();
		Assert.assertEquals(true, check);

	}

	@Test
	public void validateMerchantDetailsJwtTestForPost() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setAuthenticationType("jwt");
		merchantConfig.setRequestType("POST");
		merchantConfig.setMerchantDetails();
		
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		this.logger = Log4j.getInstance(merchantConfig);

		boolean check = MerchantUtil.validateMerchantDetails();
		Assert.assertEquals(true, check);

	}

	@Test
	public void validateMerchantDetailsJwtTestForPut() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setAuthenticationType("jwt");
		merchantConfig.setRequestType("PUT");
		merchantConfig.setMerchantDetails();
		
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		this.logger = Log4j.getInstance(merchantConfig);

		boolean check =  MerchantUtil.validateMerchantDetails();
		Assert.assertEquals(true, check);

	}

	//// Test cases for keyPassword check

	@Test
	public void checkKeyPasswordTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		
		boolean check = MerchantUtil.checkKeyPassword();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkKeyPasswordNullTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setKeyPass(null);
		merchantUtil=new MerchantUtil(merchantConfig);

		boolean check = MerchantUtil.checkKeyPassword();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkKeyPasswordEmptyTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setKeyPass("");
		merchantUtil=new MerchantUtil(merchantConfig);

		boolean check = MerchantUtil.checkKeyPassword();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkKeyPasswordNotEqualMerchantIdTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setKeyPass("test");
		merchantUtil=new MerchantUtil(merchantConfig);

		boolean check = MerchantUtil.checkKeyPassword();
		Assert.assertEquals(true, check);

	}

	// Test cases for run environment check

	@Test
	public void checkRunEvironmentTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		
		boolean check =MerchantUtil.checkRunEvironment();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkRunEvironmentForNullTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRunEnvironment(null);
		merchantUtil=new MerchantUtil(merchantConfig);
		
		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.RUN_ENVIRONMENT_ERR);
		
		MerchantUtil.checkRunEvironment();
		

	}

	@Test
	public void checkRunEvironmentForEmptyTest() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRunEnvironment("");
		merchantUtil=new MerchantUtil(merchantConfig);
		
		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.RUN_ENVIRONMENT_ERR);
		
		MerchantUtil.checkRunEvironment();
		

	}

	@Test
	public void checkRunEvironmentForProductionTest() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRunEnvironment("CyberSource.Environment.PRODUCTION");
		merchantUtil=new MerchantUtil(merchantConfig);

		boolean check =MerchantUtil.checkRunEvironment();
		Assert.assertEquals(true,check);

	}

	@Test
	public void checkRunEvironmentTestFail() throws ConfigException, IOException {
		
			merchantProp = PropertiesUtil.getMerchantProperties();
			merchantConfig = new MerchantConfig(merchantProp);
			merchantConfig.setRunEnvironment("test");
			merchantUtil=new MerchantUtil(merchantConfig);
			boolean check = false;
			try {
				check = MerchantUtil.checkRunEvironment();
			} catch (ConfigException e) {
				check = false;
			}
			Assert.assertEquals(false, check);
		}

	// Test cases for check merchant keyId

	@Test
	public void checkMerchantKeyIdTestForNull() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setMerchantKeyId(null);
		merchantUtil=new MerchantUtil(merchantConfig);
		
		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.MERCHANT_KEYID_MISSING);

		MerchantUtil.checkMerchantKeyId();

	}

	@Test
	public void checkMerchantKeyIdTestForEmpty() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setMerchantKeyId("");
		merchantUtil=new MerchantUtil(merchantConfig);
		
		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.MERCHANT_KEYID_MISSING);

		MerchantUtil.checkMerchantKeyId();
	}

	// Test cases for check merchantSecretKey

	@Test
	public void checkMerchantSecertKeyTestForNull() throws ConfigException, IOException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setMerchantSecretKey(null);
		merchantUtil=new MerchantUtil(merchantConfig);
		
		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.MERCHANTSECERT_KEY_MISSING);

		MerchantUtil.checkMerchantSecertKey();

	}

	@Test
	public void checkMerchantSecertKeyTestForEmpty() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setMerchantSecretKey("");
		merchantUtil=new MerchantUtil(merchantConfig);
		
		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.MERCHANTSECERT_KEY_MISSING);

		MerchantUtil.checkMerchantSecertKey();

	}

	// Test cases for log max size and folder check

	@Test
	public void checkLogMaxSizeAndFolderTest() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);

		boolean check = MerchantUtil.checkLogMaxSizeAndFolder();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkLogMaxSizeAndFolderTestForNull() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setLogDirectory(null);
		merchantUtil=new MerchantUtil(merchantConfig);

		boolean check = MerchantUtil.checkLogMaxSizeAndFolder();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkLogMaxSizeTestForEmpty() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setLogDirectory("");
		merchantUtil=new MerchantUtil(merchantConfig);

		boolean check = MerchantUtil.checkLogMaxSizeAndFolder();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkLogMaxSizeTestFail() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setLogDirectory(null);
		merchantUtil=new MerchantUtil(merchantConfig);

		boolean check = MerchantUtil.checkLogMaxSizeAndFolder();
		Assert.assertEquals(true, check);

	}

	// Test cases for json path check

	@Test
	public void checkJsonPathTest() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantUtil=new MerchantUtil(merchantConfig);
		
		boolean check = MerchantUtil.checkJsonPath(requestJsonPath);
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkJsonPathTestWrongRequestType() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("OPTION");
		merchantUtil=new MerchantUtil(merchantConfig);
		
		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.INVALID_REQUEST_TYPE);

		MerchantUtil.checkJsonPath(jsonPathforCheck);
		

	}

	@Test
	public void checkJsonPathNullTest() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		merchantUtil=new MerchantUtil(merchantConfig);
		
		boolean check = MerchantUtil.checkJsonPath(null);
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkJsonPathEmptyTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		merchantUtil=new MerchantUtil(merchantConfig);
		
		boolean check = MerchantUtil.checkJsonPath("");
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkJsonPathTestFileNotExist() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantUtil=new MerchantUtil(merchantConfig);
		
		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.REQUEST_JSON_ERROR);

		MerchantUtil.checkJsonPath(jsonPathforCheck);

	}

	@Test
	public void checkJsonPathTestIsrequestTypeFalse() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantUtil=new MerchantUtil(merchantConfig);
		
		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.REQUEST_JSON_ERROR);
		
		MerchantUtil.checkJsonPath(jsonPathforCheck);
		

	}
	// test cases for keyalias check

	@Test
	public void checkKeyAliasTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setKeyAlias("testrest");
		merchantUtil=new MerchantUtil(merchantConfig);

		boolean check = MerchantUtil.checkKeyAlias();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkKeyAliasTestForNull() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setKeyAlias(null);
		merchantUtil=new MerchantUtil(merchantConfig);

		boolean check = MerchantUtil.checkKeyAlias();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkKeyAliasTestForEmpty() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setKeyAlias("");
		merchantUtil=new MerchantUtil(merchantConfig);

		boolean check = MerchantUtil.checkKeyAlias();
		Assert.assertEquals(true, check);
	}

	@Test
	public void checkKeyAliasTestForWrong() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setKeyAlias("test");
		merchantUtil=new MerchantUtil(merchantConfig);

		boolean check = MerchantUtil.checkKeyAlias();
		Assert.assertEquals(true, check);

	}

	
	@Test
	public void setMerchantDetailsTestForEmpty() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setMerchantID("");
		merchantUtil=new MerchantUtil(merchantConfig);

		merchantConfig.setMerchantDetails();
		Assert.assertEquals(true, MerchantConfig.isSetMerchantDetails());

	}

	
	// test cases for check key file
	@Test
	public void CheckKeyFileTestForNull() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setKeyFilename(null);
		merchantUtil=new MerchantUtil(merchantConfig);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertEquals(true, true);
		} else {
			boolean check = MerchantUtil.checkKeyFile();
			Assert.assertEquals(true, check);
		}	

	}

	@Test
	public void CheckKeyFileTestForKeyFileNotExist() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setKeyFilename("test");
		merchantUtil=new MerchantUtil(merchantConfig);
        
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertEquals(true, true);
		} else {
			boolean check = MerchantUtil.checkKeyFile();
			Assert.assertEquals(false, check);
		}
		

	}


	// Test cases for check request type

	@Test
	public void checkRequestTypeTestForGet() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("GET");
		merchantUtil=new MerchantUtil(merchantConfig);

		boolean check = MerchantUtil.checkRequestType();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkRequestTypeTestForPost() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantUtil=new MerchantUtil(merchantConfig);

		boolean check = MerchantUtil.checkRequestType();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkRequestTypeTestForPut() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("PUT");
		merchantUtil=new MerchantUtil(merchantConfig);

		boolean check = MerchantUtil.checkRequestType();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkRequestTypeTestForDelete() throws ConfigException, IOException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("Delete");
		merchantUtil=new MerchantUtil(merchantConfig);

		boolean check = MerchantUtil.checkRequestType();
		Assert.assertEquals(true, check);

	}

	@Test
	public void checkRequestTypeTestFail() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("option");
		merchantUtil=new MerchantUtil(merchantConfig);

		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.INVALID_REQUEST_TYPE);
		MerchantUtil.checkRequestType();
		
	}

	
	@Test
	public void isRequestJsonTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		
		boolean str = merchantUtil.isRequestJson();
		Assert.assertNotNull(str);
	}
	
}
