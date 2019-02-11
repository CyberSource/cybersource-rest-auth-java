package com.cybersource.authsdk.util.test;

import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

public class MerchantConfigTest {
	
	@Mock
	MerchantConfig merchantConfig;
	
	@Mock
	RequestContext requestContext;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Properties merchantProp;
	private String requestJsonPath = "src/test/resources/request.json";
	private String jsonPathforCheck = "src/test/resources/abc.json";
	private String testJsonPath = "src/test/resources/test.json";
	

	// Test cases for request data
//	@Test
//	public void getRequestBodyTestPass() throws ConfigException, IOException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestContext = new RequestContext(merchantConfig.getLogger());
//		requestContext.setRequestJsonPath(requestJsonPath);
//
//		String requestData = requestContext.getRequestBody();
//		Assert.assertNotNull(requestData);
//	}
//
//	@Test
//	public void getRequestBodyTestFail() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestContext = new RequestContext(merchantConfig.getLogger());
//		requestContext.setRequestMethodType("POST");
//		requestContext.setRequestJsonPath(testJsonPath);
//		String Data = requestContext.getRequestBody();
//		Assert.assertNotNull(Data);
//
//	}
//
//	@Test
//	public void validateRequestDetailsHttpTestforGet() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantProp.setProperty("authenticationType", "Http_Signature");
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		requestContext=new RequestContext(merchantConfig.getLogger());
//		requestContext.setRequestMethodType("GET");
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertEquals(false,false);
//		} else {
//			Assert.assertEquals(true, requestContext.validateRequestDetails(requestJsonPath));
//		}
//	}
//
//	@Test
//	public void validateRequestDetailsHttpTestforPost() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantProp.setProperty("authenticationType", "Http_Signature");
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		requestContext=new RequestContext(merchantConfig.getLogger());
//		requestContext.setRequestMethodType("POST");
//		requestContext.setRequestJsonPath(requestJsonPath);
//		merchantConfig.getLogger();
//        
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertEquals(false,false);
//		} else {
//			Assert.assertEquals(true, requestContext.validateRequestDetails(requestJsonPath));
//		}
//	}
//	
//	@Test
//	public void validateRequestDetailsHttpTestforPut() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantProp.setProperty("authenticationType", "Http_Signature");
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		requestContext=new RequestContext(merchantConfig.getLogger());
//		requestContext.setRequestMethodType("PUT");
//		requestContext.setRequestJsonPath(requestJsonPath);
//		merchantConfig.getLogger();
//
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertEquals(false,false);
//		} else {
//			Assert.assertEquals(true, requestContext.validateRequestDetails(requestJsonPath));
//		}
//	}
//
//	@Test
//	public void validateMerchantDetailsJwtTestForGet() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantProp.setProperty("authenticationType", "jwt");
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		requestContext=new RequestContext(merchantConfig.getLogger());
//		requestContext.setRequestMethodType("GET");
//
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
//			Assert.assertNull(null);
//		} else {
//			merchantConfig.setMerchantDetails();
//			Assert.assertEquals("testrest", merchantConfig.getMerchantID());
//		}	
//
//	}

//	@Test
//	public void validateMerchantDetailsJwtTestForPost() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setAuthenticationType("jwt");
//		merchantConfig.setMerchantDetails();
//		
//		requestContext=new RequestContext(merchantConfig.getLogger());
//		requestContext.setRequestMethodType("POST");
//		requestContext.setRequestJsonPath(requestJsonPath);
//		merchantConfig.getLogger();
//        
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
//			Assert.assertNull(null);
//		} else {
//			boolean check = RequestContext.validateMerchantDetails();
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
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("PUT");
//		requestContext.setRequestJsonPath(requestJsonPath);
//		merchantConfig.getLogger();
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
//			Assert.assertNull(null);
//		} else {
//			boolean check =  RequestContext.validateMerchantDetails();
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
//		requestContext=new RequestContext();
//		
//		boolean check = RequestContext.checkKeyPassword();
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
//		requestContext=new RequestContext();
//
//		boolean check = RequestContext.checkKeyPassword();
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
//		requestContext=new RequestContext();
//
//		boolean check = RequestContext.checkKeyPassword();
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
//		requestContext=new RequestContext();
//
//		boolean check = RequestContext.checkKeyPassword();
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
//		requestContext=new RequestContext();
//		
//		boolean check =RequestContext.checkRunEvironment();
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
//		requestContext=new RequestContext();
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.RUN_ENVIRONMENT_ERR);
//		
//		RequestContext.checkRunEvironment();
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
//		requestContext=new RequestContext();
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.RUN_ENVIRONMENT_ERR);
//		
//		RequestContext.checkRunEvironment();
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
//		requestContext=new RequestContext();
//
//		boolean check =RequestContext.checkRunEvironment();
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
//			requestContext=new RequestContext();
//			boolean check = false;
//			try {
//				check = RequestContext.checkRunEvironment();
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
//		requestContext=new RequestContext();
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.MERCHANT_KEYID_MISSING);
//
//		RequestContext.checkMerchantKeyId();
//
//	}
//
//	@Test
//	public void checkMerchantKeyIdTestForEmpty() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setMerchantKeyId("");
//		requestContext=new RequestContext();
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.MERCHANT_KEYID_MISSING);
//
//		RequestContext.checkMerchantKeyId();
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
//		requestContext=new RequestContext();
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.MERCHANTSECERT_KEY_MISSING);
//
//		RequestContext.checkMerchantSecertKey();
//
//	}
//
//	@Test
//	public void checkMerchantSecertKeyTestForEmpty() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setMerchantSecretKey("");
//		requestContext=new RequestContext();
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.MERCHANTSECERT_KEY_MISSING);
//
//		RequestContext.checkMerchantSecertKey();
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
//		requestContext=new RequestContext();
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
//		requestContext=new RequestContext();
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
//		requestContext=new RequestContext();
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
//		requestContext=new RequestContext();
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
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("POST");
//		
//		boolean check = RequestContext.checkJsonPath(requestJsonPath);
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void checkJsonPathTestWrongRequestType() throws IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("OPTION");
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.INVALID_REQUEST_TYPE);
//
//		RequestContext.checkJsonPath(jsonPathforCheck);
//		
//
//	}
//
//	@Test
//	public void checkJsonPathNullTest() throws IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("GET");
//		
//		boolean check = RequestContext.checkJsonPath(null);
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void checkJsonPathEmptyTest() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("GET");
//		
//		boolean check = RequestContext.checkJsonPath("");
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void checkJsonPathTestFileNotExist() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("POST");
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.REQUEST_JSON_ERROR);
//
//		RequestContext.checkJsonPath(jsonPathforCheck);
//
//	}
//
//	@Test
//	public void checkJsonPathTestIsrequestTypeFalse() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("POST");
//		
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.REQUEST_JSON_ERROR);
//		
//		RequestContext.checkJsonPath(jsonPathforCheck);
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
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("POST");
//
//		boolean check = RequestContext.checkKeyAlias();
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
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("POST");
//
//		boolean check = RequestContext.checkKeyAlias();
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
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("POST");
//
//		boolean check = RequestContext.checkKeyAlias();
//		Assert.assertEquals(true, check);
//	}
//
//	@Test
//	public void checkKeyAliasTestForWrong() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setKeyAlias("test");
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("POST");
//
//		boolean check = RequestContext.checkKeyAlias();
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
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("POST");
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
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("POST");
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
//			Assert.assertEquals(true, true);
//		} else {
//			boolean check = RequestContext.checkKeyFile();
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
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("POST");
//        
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
//			Assert.assertEquals(true, true);
//		} else {
//			boolean check = RequestContext.checkKeyFile();
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
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("GET");
//
//		boolean check = RequestContext.isRequestTypeMethodValid();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void isRequestTypeMethodValidTestForPost() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("POST");
//
//		boolean check = RequestContext.isRequestTypeMethodValid();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void isRequestTypeMethodValidTestForPut() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("PUT");
//
//		boolean check = RequestContext.isRequestTypeMethodValid();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void isRequestTypeMethodValidTestForDelete() throws ConfigException, IOException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("Delete");
//
//		boolean check = RequestContext.isRequestTypeMethodValid();
//		Assert.assertEquals(true, check);
//
//	}
//
//	@Test
//	public void isRequestTypeMethodValidTestFail() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestContext=new RequestContext();
//		requestContext.setRequestMethodType("option");
//
//		thrown.expect(ConfigException.class);
//		thrown.expectMessage(GlobalLabelParameters.INVALID_REQUEST_TYPE);
//		RequestContext.isRequestTypeMethodValid();
//		
//	}
//
//	
//	@Test
//	public void isRequestJsonTest() throws ConfigException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestContext=new RequestContext();
//		
//		boolean str = requestContext.isRequestJson();
//		Assert.assertNotNull(str);
//	}
//	
}
