package com.cybersource.authsdk.jwt.test;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.jwt.KeyCertificateGenerator;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.PropertiesUtil;

public class KeyCertificateGeneratorTest {

	private String requestJsonPath = "src/test/resources/request.json";
	@Mock
	KeyCertificateGenerator keyCertificateGenerator;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	MerchantConfig merchantConfig;
	
	@Mock
	MerchantUtil merchantUtil;

	private Properties merchantProp;

	@Test
	public void initializeCertificateTestPass()
			throws CertificateException, NoSuchAlgorithmException, KeyStoreException, Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantUtil.setRequestType("POST");

		MerchantUtil.validateMerchantDetails();

		X509Certificate certificate =null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(certificate);
		} else {
			MerchantUtil.checkKeyFile();
			certificate = KeyCertificateGenerator.initializeCertificate(merchantConfig);
			Assert.assertNotNull(certificate);
		}

	}

	@Test
	public void initializePrivateKeyTestPass()
			throws CertificateException, NoSuchAlgorithmException, KeyStoreException, Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantUtil.setRequestType("POST");

		MerchantUtil.validateMerchantDetails();

		RSAPrivateKey privateKey = null;
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(privateKey);
		} else {
			MerchantUtil.checkKeyFile();
			privateKey = KeyCertificateGenerator.initializePrivateKey(merchantConfig);
			Assert.assertNotNull(privateKey);
		}

	}

	@Test
	public void initializeRecipientCertificateTestPass()
			throws CertificateException, NoSuchAlgorithmException, KeyStoreException, Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		merchantConfig.setRecipientID("test");
		merchantUtil=new MerchantUtil(merchantConfig);
		merchantUtil.setRequestJsonPath(requestJsonPath);
		merchantUtil.setRequestType("POST");

		MerchantUtil.validateMerchantDetails();
		
		keyCertificateGenerator = new KeyCertificateGenerator();
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			thrown.expect(ConfigException.class);
			KeyCertificateGenerator.initializeRecipientCertificate(merchantConfig);
			thrown.expectMessage("merchant config fields missing: recipientId, key file");
		} else {
			MerchantUtil.checkKeyFile();
			X509Certificate certificate= KeyCertificateGenerator.initializeRecipientCertificate(merchantConfig);
			Assert.assertNotNull(certificate);
		}
	}

	@Test
	public void initializeRecipientCertificateTestFail()
			throws CertificateException, NoSuchAlgorithmException, KeyStoreException, Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = MerchantConfig.getInstance(merchantProp);
		merchantConfig.setMerchantDetails();
		merchantConfig.setRecipientID(null);;

		thrown.expect(ConfigException.class);
		thrown.expectMessage("merchant config fields missing: recipientId, key file");

		keyCertificateGenerator = new KeyCertificateGenerator();
		KeyCertificateGenerator.initializeRecipientCertificate(merchantConfig);

	}

}
