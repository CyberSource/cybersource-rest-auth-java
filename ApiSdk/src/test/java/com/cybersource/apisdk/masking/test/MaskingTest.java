package com.cybersource.apisdk.masking.test;

import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.apisdk.masking.Masking;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.PropertiesUtil;

import junit.framework.Assert;

public class MaskingTest {
	
	@Mock
	MerchantConfig merchantConfig;

	private String requestJsonPath = "src/test/resources/request.json";
	private Properties merchantProp;
	Logger logger;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void prepareMaskingTest() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		MerchantConfig merchantConfig = new MerchantConfig(merchantProp);
		
		logger = Log4j.getInstance(merchantConfig);
		
		thrown.expect(Exception.class);
		
		Masking.prepareMasking(null);
		
	}

	
	@Test
	public void prepareMaskingTestFail() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		MerchantConfig merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestJsonPath(requestJsonPath);
		
		
		logger = Log4j.getInstance(merchantConfig);
		
		thrown.expect(Exception.class);
		
		Masking masking=new Masking(merchantConfig.getRequestData(), logger);
		String str=masking.prepareMasking(null);
		Assert.assertNotNull(str);
	}
}
