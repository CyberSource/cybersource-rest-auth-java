package com.cybersource.apisdk.masking.test;

import java.util.Properties;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.apisdk.masking.Masking;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;

import junit.framework.Assert;

public class MaskingTest {
	
	@Mock
	MerchantConfig merchantConfig;
	@Mock
	MerchantUtil merchantUtil;

	private String requestJsonPath = "src/test/resources/request.json";
	private Properties merchantProp;
		
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	
	@Test
	public void prepareMaskingTestFail() throws Exception {
		merchantProp = PropertiesUtil.getMerchantProperties();
		MerchantConfig merchantConfig = new MerchantConfig(merchantProp);
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		
		thrown.expect(Exception.class);
		
		String str=Masking.prepareMasking(null);
		Assert.assertNotNull(str);
	}
}
