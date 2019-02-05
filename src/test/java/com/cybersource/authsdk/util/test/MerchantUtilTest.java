package com.cybersource.authsdk.util.test;

import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.authsdk.core.AuthenticationRequestContext;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

public class MerchantUtilTest {
	
	@Mock
	MerchantConfig merchantConfig;
	
	@Mock
	AuthenticationRequestContext merchantUtil;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Properties merchantProp;
	private String requestJsonPath = "src/test/resources/request.json";
	private String jsonPathforCheck = "src/test/resources/abc.json";
	private String testJsonPath = "src/test/resources/test.json";
	

	// Test cases for request data
//	@Test
//	public void getRequestDataTestPass() throws ConfigException, IOException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil = new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestJsonPath(requestJsonPath);
//
//		String requestData = merchantUtil.getRequestData();
//		Assert.assertNotNull(requestData);
//	}
//
//	@Test
//	public void getRequestDataTestFail() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil = new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("POST");
//		merchantUtil.setRequestJsonPath(testJsonPath);
//		String Data = merchantUtil.getRequestData();
//		Assert.assertNotNull(Data);
//
//	}
//
//	// test cases for validate merchant detail
//	@Test
//	public void validateMerchantDetailsHttpTestforGet() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantConfig.setAuthenticationType("Http_Signature");
//		merchantUtil.setRequestType("GET");
//		merchantConfig.setMerchantDetails();
//		merchantConfig.getLogger();
//		
//		boolean check = false;
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertEquals(false,check);
//		} else {
//			check = AuthenticationRequestContext.validateMerchantDetails();
//			Assert.assertEquals(true,check);
//		}
//	}
//
//	@Test
//	public void validateMerchantDetailsHttpTestforPost() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantConfig.setAuthenticationType("Http_Signature");
//		merchantUtil.setRequestType("POST");
//		merchantConfig.setMerchantDetails();
//		merchantUtil.setRequestJsonPath(requestJsonPath);
//		merchantConfig.getLogger();
//        
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(null);
//		} else {
//			boolean check = AuthenticationRequestContext.validateMerchantDetails();
//			Assert.assertEquals(true, check);
//		}
//	}
//
//	@Test
//	public void validateMerchantDetailsHttpTestforPut() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		
//		merchantConfig.setAuthenticationType("Http_Signature");
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("PUT");
//		
//		merchantUtil.setRequestJsonPath(requestJsonPath);
//		merchantConfig.getLogger();
//
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(null);
//		} else {
//			boolean check = AuthenticationRequestContext.validateMerchantDetails();
//			Assert.assertEquals(true, check);
//		}
//	}
//
//	@Test
//	public void validateMerchantDetailsJwtTestForGet() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setAuthenticationType("jwt");
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("GET");
//
//		merchantConfig.getLogger();
//
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
//			Assert.assertNull(null);
//		} else {
//			boolean check = AuthenticationRequestContext.validateMerchantDetails();
//			Assert.assertEquals(true, check);
//		}	
//
//	}
//
//	@Test
//	public void validateMerchantDetailsJwtTestForPost() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setAuthenticationType("jwt");
//		merchantConfig.setMerchantDetails();
//		
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("POST");
//		merchantUtil.setRequestJsonPath(requestJsonPath);
//		merchantConfig.getLogger();
//        
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
//			Assert.assertNull(null);
//		} else {
//			boolean check = AuthenticationRequestContext.validateMerchantDetails();
//			Assert.assertEquals(true, check);
//		}		
//
//	}
//
//	@Test
//	public void validateMerchantDetailsJwtTestForPut() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setAuthenticationType("jwt");
//		merchantConfig.setMerchantDetails();
//		
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("PUT");
//		merchantUtil.setRequestJsonPath(requestJsonPath);
//		merchantConfig.getLogger();
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
//			Assert.assertNull(null);
//		} else {
//			boolean check =  AuthenticationRequestContext.validateMerchantDetails();
//			Assert.assertEquals(true, check);
//		}
//	}
//
//	//// Test cases for keyPassword check
//
//	@Test
//	public void checkKeyPasswordTest() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		
//		boolean check = AuthenticationRequestContext.checkKeyPassword();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void checkKeyPasswordNullTest() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setKeyPass(null);
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//
//		boolean check = AuthenticationRequestContext.checkKeyPassword();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void checkKeyPasswordEmptyTest() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setKeyPass("");
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//
//		boolean check = AuthenticationRequestContext.checkKeyPassword();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void checkKeyPasswordNotEqualMerchantIdTest() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setKeyPass("test");
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//
//		boolean check = AuthenticationRequestContext.checkKeyPassword();
//		Assert.assertEquals(true, check);
//
//	}
//
//	// Test cases for run environment check
//
//	@Test
//	public void checkRunEvironmentTest() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		
//		boolean check =AuthenticationRequestContext.checkRunEvironment();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void checkRunEvironmentForNullTest() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setRunEnvironment(null);
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.RUN_ENVIRONMENT_ERR);
//		
//		AuthenticationRequestContext.checkRunEvironment();
//		
//
//	}
//
//	@Test
//	public void checkRunEvironmentForEmptyTest() throws IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setRunEnvironment("");
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.RUN_ENVIRONMENT_ERR);
//		
//		AuthenticationRequestContext.checkRunEvironment();
//		
//
//	}
//
//	@Test
//	public void checkRunEvironmentForProductionTest() throws IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setRunEnvironment("CyberSource.Environment.PRODUCTION");
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//
//		boolean check =AuthenticationRequestContext.checkRunEvironment();
//		Assert.assertEquals(true,check);
//
//	}
//
//	@Test
//	public void checkRunEvironmentTestFail() throws ConfigException, IOException {
//		
//			merchantProp = PropertiesUtil.getMerchantProperties();
//			merchantConfig = MerchantConfig.getInstance(merchantProp);
//			merchantConfig.setMerchantDetails();
//			merchantConfig.setRunEnvironment("");
//			merchantUtil=new AuthenticationRequestContext(merchantConfig);
//			boolean check = false;
//			try {
//				check = AuthenticationRequestContext.checkRunEvironment();
//			} catch (ConfigException e) {
//				check = false;
//			}
//			Assert.assertEquals(false, check);
//		}
//
//	// Test cases for check merchant keyId
//
//	@Test
//	public void checkMerchantKeyIdTestForNull() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setMerchantKeyId(null);
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.MERCHANT_KEYID_MISSING);
//
//		AuthenticationRequestContext.checkMerchantKeyId();
//
//	}
//
//	@Test
//	public void checkMerchantKeyIdTestForEmpty() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setMerchantKeyId("");
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.MERCHANT_KEYID_MISSING);
//
//		AuthenticationRequestContext.checkMerchantKeyId();
//	}
//
//	// Test cases for check merchantSecretKey
//
//	@Test
//	public void checkMerchantSecertKeyTestForNull() throws ConfigException, IOException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setMerchantSecretKey(null);
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.MERCHANTSECERT_KEY_MISSING);
//
//		AuthenticationRequestContext.checkMerchantSecertKey();
//
//	}
//
//	@Test
//	public void checkMerchantSecertKeyTestForEmpty() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setMerchantSecretKey("");
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.MERCHANTSECERT_KEY_MISSING);
//
//		AuthenticationRequestContext.checkMerchantSecertKey();
//
//	}
//
//	// Test cases for log max size and folder check
//
//	@Test
//	public void checkLogMaxSizeAndFolderTest() throws IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//
//		boolean check = merchantConfig.checkLogMaxSizeAndFolder();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void checkLogMaxSizeAndFolderTestForNull() throws IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setLogDirectory(null);
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//
//		boolean check = merchantConfig.checkLogMaxSizeAndFolder();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void checkLogMaxSizeTestForEmpty() throws IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setLogDirectory("");
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//
//		boolean check = merchantConfig.checkLogMaxSizeAndFolder();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void checkLogMaxSizeTestFail() throws IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setLogDirectory(null);
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//
//		boolean check = merchantConfig.checkLogMaxSizeAndFolder();
//		Assert.assertEquals(true, check);
//
//	}
//
//	// Test cases for json path check
//
//	@Test
//	public void checkJsonPathTest() throws IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("POST");
//		
//		boolean check = AuthenticationRequestContext.checkJsonPath(requestJsonPath);
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void checkJsonPathTestWrongRequestType() throws IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("OPTION");
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.INVALID_REQUEST_TYPE);
//
//		AuthenticationRequestContext.checkJsonPath(jsonPathforCheck);
//		
//
//	}
//
//	@Test
//	public void checkJsonPathNullTest() throws IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("GET");
//		
//		boolean check = AuthenticationRequestContext.checkJsonPath(null);
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void checkJsonPathEmptyTest() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("GET");
//		
//		boolean check = AuthenticationRequestContext.checkJsonPath("");
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void checkJsonPathTestFileNotExist() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("POST");
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.REQUEST_JSON_ERROR);
//
//		AuthenticationRequestContext.checkJsonPath(jsonPathforCheck);
//
//	}
//
//	@Test
//	public void checkJsonPathTestIsrequestTypeFalse() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("POST");
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.REQUEST_JSON_ERROR);
//		
//		AuthenticationRequestContext.checkJsonPath(jsonPathforCheck);
//		
//
//	}
//	// test cases for keyalias check
//
//	@Test
//	public void checkKeyAliasTest() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setKeyAlias("testrest");
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("POST");
//
//		boolean check = AuthenticationRequestContext.checkKeyAlias();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void checkKeyAliasTestForNull() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setKeyAlias(null);
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("POST");
//
//		boolean check = AuthenticationRequestContext.checkKeyAlias();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void checkKeyAliasTestForEmpty() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setKeyAlias("");
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("POST");
//
//		boolean check = AuthenticationRequestContext.checkKeyAlias();
//		Assert.assertEquals(true, check);
//	}
//
//	@Test
//	public void checkKeyAliasTestForWrong() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setKeyAlias("test");
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("POST");
//
//		boolean check = AuthenticationRequestContext.checkKeyAlias();
//		Assert.assertEquals(true, check);
//
//	}
//
//	
//	@Test
//	public void setMerchantDetailsTest() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("POST");
//		merchantConfig.setRunEnvironment(null);
//       
//		Boolean check = true;
//		try {
//			merchantConfig.setMerchantDetails();
//		} catch (ConfigException e) {
//			check = false;
//		}
//		Assert.assertEquals(true, check);
//	}
//
//	
//	// test cases for check key file
//	@Test
//	public void CheckKeyFileTestForNull() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setKeyFilename(null);
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("POST");
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
//			Assert.assertEquals(true, true);
//		} else {
//			boolean check = AuthenticationRequestContext.checkKeyFile();
//			Assert.assertEquals(true, check);
//		}	
//
//	}
//
//	@Test
//	public void CheckKeyFileTestForKeyFileNotExist() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setKeyFilename("test");
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("POST");
//        
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
//			Assert.assertEquals(true, true);
//		} else {
//			boolean check = AuthenticationRequestContext.checkKeyFile();
//			Assert.assertEquals(false, check);
//		}
//		
//
//	}
//
//
//	// Test cases for check request type
//
//	@Test
//	public void isRequestTypeMethodValidTestForGet() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("GET");
//
//		boolean check = AuthenticationRequestContext.isRequestTypeMethodValid();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void isRequestTypeMethodValidTestForPost() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("POST");
//
//		boolean check = AuthenticationRequestContext.isRequestTypeMethodValid();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void isRequestTypeMethodValidTestForPut() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("PUT");
//
//		boolean check = AuthenticationRequestContext.isRequestTypeMethodValid();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void isRequestTypeMethodValidTestForDelete() throws ConfigException, IOException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("Delete");
//
//		boolean check = AuthenticationRequestContext.isRequestTypeMethodValid();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void isRequestTypeMethodValidTestFail() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		merchantUtil.setRequestType("option");
//
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.INVALID_REQUEST_TYPE);
//		AuthenticationRequestContext.isRequestTypeMethodValid();
//		
//	}
//
//	
//	@Test
//	public void isRequestJsonTest() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantUtil=new AuthenticationRequestContext(merchantConfig);
//		
//		boolean str = merchantUtil.isRequestJson();
//		Assert.assertNotNull(str);
//	}
	
}
