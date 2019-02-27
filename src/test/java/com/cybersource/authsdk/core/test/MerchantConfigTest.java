package com.cybersource.authsdk.core.test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
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

	@Before
	public void resetSingleton()
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field instance = MerchantConfig.class.getDeclaredField("instance");
		instance.setAccessible(true);
		instance.set(null, null);
		merchantProp = null;
	}

	@Test
	public void setMerchantDetailsJwtTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "JWT");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertEquals(merchantConfig.getAuthenticationType(), "JWT");
	}

	@Test
	public void setMerchantDetailsHttpTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "HTTP_Signature");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertEquals(merchantConfig.getAuthenticationType(), "HTTP_Signature");
	}

	@Test
	public void checkKeyPasswordTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "JWT");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertNotNull(merchantConfig.getKeyPass());

	}

	@Test
	public void checkKeyPasswordNullTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "JWT");
		merchantProp.keySet().remove("keyPass");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertNotNull(merchantConfig.getKeyPass());
	}

	@Test
	public void checkKeyPasswordEmptyTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "JWT");
		merchantProp.put("keyPass", "");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertNotNull(merchantConfig.getKeyPass());
	}

	@Test
	public void checkKeyAliasTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "JWT");
		merchantProp.put("keyAlias", "testrest");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertEquals(merchantConfig.getMerchantID(), merchantConfig.getKeyAlias());
	}

	@Test
	public void checkKeyAliasTestForNull() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "JWT");
		merchantProp.keySet().remove("keyAlias");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertEquals(merchantConfig.getMerchantID(), merchantConfig.getKeyAlias());
	}

	@Test
	public void checkKeyAliasTestForEmpty() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "JWT");
		merchantProp.put("keyAlias", "");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertEquals(merchantConfig.getMerchantID(), merchantConfig.getKeyAlias());
	}

	@Test
	public void checkKeyAliasTestForWrong() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "JWT");
		merchantProp.put("keyAlias", "test");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertEquals(merchantConfig.getMerchantID(), merchantConfig.getKeyAlias());
	}

	// Test cases for run environment check

	@Test
	public void checkRunEvironmentTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("runEnvironment", "CyberSource.Environment.SANDBOX");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertNotNull(merchantConfig.getRunEnvironment());

	}

	@Test
	public void checkRunEvironmentForNullTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.keySet().remove("runEnvironment");

		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.RUN_ENVIRONMENT_ERR);

		merchantConfig = MerchantConfig.getInstance(merchantProp);
	}

	@Test
	public void checkRunEvironmentForEmptyTest() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("runEnvironment", "");

		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.RUN_ENVIRONMENT_ERR);

		merchantConfig = MerchantConfig.getInstance(merchantProp);
	}

	@Test
	public void checkRunEvironmentForProductionTest() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("runEnvironment", "CyberSource.Environment.PRODUCTION");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertEquals(GlobalLabelParameters.PRODUCTION_URL, merchantConfig.getRequestHost());
	}

	// Test cases for check merchant keyId

	@Test
	public void checkMerchantKeyIdTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "HTTP_Signature");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertNotNull(merchantConfig.getMerchantKeyId());
	}

	@Test
	public void checkMerchantKeyIdTestForNull() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "HTTP_Signature");
		merchantProp.keySet().remove("merchantKeyId");

		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.MERCHANT_KEYID_MISSING);

		merchantConfig = MerchantConfig.getInstance(merchantProp);
	}

	@Test
	public void checkMerchantKeyIdTestForEmpty() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "HTTP_Signature");
		merchantProp.put("merchantKeyId", "");

		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.MERCHANT_KEYID_MISSING);

		merchantConfig = MerchantConfig.getInstance(merchantProp);
	}

	// Test cases for check merchantSecretKey

	@Test
	public void checkMerchantSecertKeyTest() throws ConfigException, IOException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "HTTP_Signature");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertNotNull(merchantConfig.getMerchantSecretKey());
	}

	@Test
	public void checkMerchantSecertKeyTestForNull() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "HTTP_Signature");
		merchantProp.keySet().remove("merchantsecretKey");

		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.MERCHANTSECERT_KEY_MISSING);

		merchantConfig = MerchantConfig.getInstance(merchantProp);
	}

	@Test
	public void checkMerchantSecertKeyTestForEmpty() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "HTTP_Signature");
		merchantProp.put("merchantsecretKey", "");

		thrown.expect(ConfigException.class);
		thrown.expectMessage(GlobalLabelParameters.MERCHANTSECERT_KEY_MISSING);

		merchantConfig = MerchantConfig.getInstance(merchantProp);
	}

	// test cases for check key file
	@Test
	public void CheckKeyFileTestForNull() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "JWT");
		merchantProp.keySet().remove("keyFilename");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertEquals(merchantConfig.getMerchantID(), merchantConfig.getKeyFilename());

	}

	@Test
	public void CheckKeyFileTestForEmpty() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "JWT");
		merchantProp.put("keyFileName", "");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertEquals(merchantConfig.getMerchantID(), merchantConfig.getKeyFilename());

	}

	@Test
	public void CheckKeyFileTestForKeyFileNotExist() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("authenticationType", "JWT");
		merchantProp.put("keyFileName", "test");

		thrown.expect(ConfigException.class);
		thrown.expectMessage(
				GlobalLabelParameters.KEY_FILE_NOT_FOUND.concat(merchantProp.getProperty("keysDirectory")).concat("/")
						.concat(merchantProp.getProperty("keyFileName")).concat(GlobalLabelParameters.P12_EXTENSION));

		merchantConfig = MerchantConfig.getInstance(merchantProp);
	}

	// Test cases for log max size and folder check
	@Test
	public void checkLogDirectoryTestForNull() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.keySet().remove("logDirectory");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertEquals("log", merchantConfig.getLogDirectory());

	}

	@Test
	public void checkLogDirectoryTestForEmpty() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("logDirectory", "");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertEquals("log", merchantConfig.getLogDirectory());
	}

	@Test
	public void checkLogFilenameTestNull() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.keySet().remove("logFilename");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertEquals("cybs", merchantConfig.getLogFilename());
	}

	@Test
	public void checkLogFilenameTestEmpty() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("logFilename", "");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertEquals("cybs", merchantConfig.getLogFilename());
	}

	@Test
	public void checkLogMaximumSizeTestNull() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.keySet().remove("logMaximumSize");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertEquals(GlobalLabelParameters.DEFAULT_LOG_SIZE, merchantConfig.getLogMaximumSize());
	}

	@Test
	public void checkLogMaximumSizeTestEmpty() throws IOException, ConfigException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.put("logMaximumSize", "");
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		Assert.assertEquals(GlobalLabelParameters.DEFAULT_LOG_SIZE, merchantConfig.getLogMaximumSize());
	}

	@Test
	public void setMerchantDetailsTest() throws ConfigException, IOException {
		merchantProp = PropertiesUtil.getMerchantProperties();
		Boolean check = true;
		try {
			merchantConfig = MerchantConfig.getInstance(merchantProp);
		} catch (ConfigException e) {
			check = false;
		}
		Assert.assertEquals(true, check);
	}
}
