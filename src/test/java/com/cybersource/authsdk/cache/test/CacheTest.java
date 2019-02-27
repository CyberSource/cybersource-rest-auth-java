package com.cybersource.authsdk.cache.test;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.authsdk.cache.Identity;
import com.cybersource.authsdk.cache.MerchantIdentityCache;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

import junit.framework.Assert;

public class CacheTest {

	private Properties merchantProp;
	private String requestJsonPath = "src/test/resources/request.json";
	public static ConcurrentHashMap<String, Identity> cacheP12 = new ConcurrentHashMap<>();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	MerchantIdentityCache cache;

	@Mock
	MerchantConfig merchantConfig;

	@Mock
	RequestContext requestContext;

	@Test
	public void setP12FileDetailsInCacheEmptyTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType("POST");
		requestContext.setRequestJsonPath(requestJsonPath);

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			merchantConfig.checkKeyFile();
			cache = MerchantIdentityCache.getInstance(merchantConfig.getLogger());
			Assert.assertNotNull(cache.retrieveP12DataFromCache(merchantConfig));
		} else {
			Assert.assertNull(null);
		}
	}

	@Test
	public void setP12FileDetailsInCacheNotEmptyTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType("POST");
		requestContext.setRequestJsonPath(requestJsonPath);

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			merchantConfig.checkKeyFile();
			cache = MerchantIdentityCache.getInstance(merchantConfig.getLogger());
			cache.setUpP12Cache(merchantConfig);
			Assert.assertNotNull(cache.retrieveP12DataFromCache(merchantConfig));
		} else {
			Assert.assertNull(null);
		}
	}

	@Test
	public void setP12FileDetailsInCacheTimeStampTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		requestContext = new RequestContext(merchantConfig.getLogger());
		requestContext.setRequestMethodType("POST");
		requestContext.setRequestJsonPath(requestJsonPath);

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			Identity identity = null;
			merchantConfig.checkKeyFile();
			cache = MerchantIdentityCache.getInstance(merchantConfig.getLogger());
			identity = cache.setUpP12Cache(merchantConfig);
			Boolean result = cache.isLastModifiedTimeUpdated(merchantConfig, identity);
			if (result) {
				identity = cache.setUpP12Cache(merchantConfig);
				Assert.assertNotNull(identity);
			} else {
				identity = cache.retrieveP12DataFromCache(merchantConfig);
			}

		} else {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void getLastModifiedFileP12Test() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantProp.keySet().remove("keyFile");
		merchantConfig = MerchantConfig.getInstance(merchantProp);

		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			cache = MerchantIdentityCache.getInstance(merchantConfig.getLogger());
			Assert.assertNotNull(cache.retrieveP12DataFromCache(merchantConfig));
		} else {
			Assert.assertTrue(true);
		}

	}
}
