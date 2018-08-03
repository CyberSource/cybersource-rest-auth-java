package com.cybersource.authsdk.jwt.test;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.jwt.KeyCertificateGenerator;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.PropertiesUtil;

public class KeyCertificateGeneratorTest {

	@Mock
	KeyCertificateGenerator keyCertificateGenerator;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	MerchantConfig merchantConfig;

	private Properties merchantProp;

	@Mock
	Logger logger;

	@Test
	public void initializeCertificateTestPass()
			throws CertificateException, NoSuchAlgorithmException, KeyStoreException, Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);
		merchantConfig.checkKeyFile();

		keyCertificateGenerator = new KeyCertificateGenerator();
		X509Certificate certificate = keyCertificateGenerator.initializeCertificate(merchantConfig);
		Assert.assertNotNull(certificate);

	}

	@Test
	public void initializePrivateKeyTestPass()
			throws CertificateException, NoSuchAlgorithmException, KeyStoreException, Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);
		merchantConfig.checkKeyFile();

		keyCertificateGenerator = new KeyCertificateGenerator();
		RSAPrivateKey privateKey = keyCertificateGenerator.initializePrivateKey(merchantConfig);
		Assert.assertNotNull(privateKey);

	}

	@Test
	public void initializeRecipientCertificateTestPass()
			throws CertificateException, NoSuchAlgorithmException, KeyStoreException, Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);
		merchantConfig.setRequestType("POST");
		merchantConfig.setRecipientID("test");

		logger = Log4j.getInstance(merchantConfig);
		merchantConfig.validateMerchantDetails(logger);
		merchantConfig.checkKeyFile();

		keyCertificateGenerator = new KeyCertificateGenerator();
		X509Certificate certificate = keyCertificateGenerator.initializeRecipientCertificate(merchantConfig);
		Assert.assertNotNull(certificate);
	}

	@Test
	public void initializeRecipientCertificateTestFail()
			throws CertificateException, NoSuchAlgorithmException, KeyStoreException, Exception {

		merchantProp = PropertiesUtil.getMerchantProperties();
		merchantConfig = new MerchantConfig(merchantProp);

		thrown.expect(ConfigException.class);
		thrown.expectMessage("merchant config fields missing: recipientId, key file");

		keyCertificateGenerator = new KeyCertificateGenerator();
		X509Certificate certificate = keyCertificateGenerator.initializeRecipientCertificate(merchantConfig);

	}

}
