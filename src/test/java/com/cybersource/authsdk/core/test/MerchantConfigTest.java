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
import com.cybersource.authsdk.util.PropertiesUtil;

public class MerchantConfigTest {

	@Mock
	MerchantConfig merchantConfig;

	@Mock
	Logger logger;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Properties merchantProp;

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
	public void setPropsTest() throws  IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setProps(merchantProp);
		Properties str = merchantConfig.getProps();
		Assert.assertNotNull(str);
	}

	@Test
	public void setURLTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setUrl("testrest");
		String str = merchantConfig.getUrl();
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
	public void setRequestHostTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setRequestHost("apitest.cybersource,com");
		String str = merchantConfig.getRequestHost();
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
	
	// test case for merchantDetails
		@Test
		public void setMerchantDetailsTestForNull() throws ConfigException, IOException {
			merchantProp = PropertiesUtil.getMerchantProperties();
			merchantConfig = new MerchantConfig(merchantProp);
			merchantConfig.setRequestType("POST");
			merchantConfig.setMerchantID(null);
			
			merchantConfig.setMerchantDetails();
			Assert.assertEquals(true, MerchantConfig.isSetMerchantDetails);
		}

	@Test
	public void setMerchantDetailsFail() throws NullPointerException, Exception {
		merchantProp = null;
		merchantConfig = new MerchantConfig(merchantProp);

		thrown.expect(NullPointerException.class);
		merchantConfig.setMerchantDetails();
	}

	@Test
	public void setProxyAddressTest() throws ConfigException {
		merchantConfig = new MerchantConfig();
		merchantConfig.setProxyAddress("userproxy.visa.com");
		String str = merchantConfig.getProxyAddress();
		Assert.assertNotNull(str);
	}

	// test cases for get Boolean property

	@Test
	public void getBooleanPropertyTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		String prop = "authenticationType";
		thrown.expect(ConfigException.class);
		thrown.expectMessage(prop + " has an invalid value.");

		merchantConfig.getBooleanProperty(merchantConfig.getMerchantID(), prop, true);
	}

	// test cases for get Integer property

	@Test
	public void getIntegerPropertyTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		String prop = "authenticationType";
		thrown.expect(ConfigException.class);
		thrown.expectMessage(prop + " has an invalid value.");

		merchantConfig.getIntegerProperty(merchantConfig.getMerchantID(), prop, 1);
	}
}
