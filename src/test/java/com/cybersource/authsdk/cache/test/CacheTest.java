package com.cybersource.authsdk.cache.test;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.authsdk.cache.Cache;
import com.cybersource.authsdk.cache.Identity;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;

import junit.framework.Assert;

public class CacheTest {

	private Properties merchantProp;
	private String requestJsonPath = "src/test/resources/request.json";
	public static ConcurrentHashMap<String, Identity> cacheP12 = new ConcurrentHashMap<>();

	@Mock
	Logger logger;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	Cache cache;
	@Mock
	MerchantConfig merchantConfig;
	
	@Mock
	MerchantUtil merchantUtil;

	@Test
	public void setP12FileDetailsInCacheEmptyTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantConfig.setRequestType("POST");
		merchantUtil.setRequestJsonPath(requestJsonPath);

		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails(logger);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			MerchantUtil.checkKeyFile();
			cache = new Cache(merchantConfig);
			cache.setP12FileDetailsInCache();
		}
		Assert.assertNotNull(cacheP12);
	}

	@Test
	public void setP12FileDetailsInCacheNotEmptyTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantConfig.setRequestType("POST");
		merchantUtil.setRequestJsonPath(requestJsonPath);

		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails(logger);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			MerchantUtil.checkKeyFile();
			cache = new Cache(merchantConfig);
			cache.setUpP12Cache();
			cache.setP12FileDetailsInCache();
		}
		Assert.assertNotNull(cacheP12);
	}

	@Test
	public void setP12FileDetailsInCacheTimeStampTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantConfig.setRequestType("POST");
		merchantUtil.setRequestJsonPath(requestJsonPath);

		logger = Log4j.getInstance(merchantConfig);
		MerchantUtil.validateMerchantDetails(logger);
		
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			MerchantUtil.checkKeyFile();
			cache = new Cache(merchantConfig);
			cache.setUpP12Cache();
			Cache.lastModifiedTime = 1528;
			cache.setP12FileDetailsInCache();
		}
		Assert.assertNotNull(cacheP12);
	}
	
	@Test
	public void getLastModifiedFileP12Test() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setKeyFilename(null);

		thrown.expect(Exception.class);

		cache = new Cache(merchantConfig);
		cache.getLastModifiedFileP12();
	}
}
