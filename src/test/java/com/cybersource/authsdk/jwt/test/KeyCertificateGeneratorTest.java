package com.cybersource.authsdk.jwt.test;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Properties;

import com.cybersource.authsdk.core.ConfigException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.jwt.KeyCertificateGenerator;
import com.cybersource.authsdk.util.GlobalLabelParameters;

public class KeyCertificateGeneratorTest {

    @Mock
    MerchantConfig merchantConfig;

    private Properties props = new Properties();

    @Before
    public void setUp() throws ConfigException {

        props.setProperty("authenticationType", "http_signature");
        props.setProperty("merchantID", "testrest");
        props.setProperty("runEnvironment", "apitest.cybersource.com");
        props.setProperty("requestJsonPath", "resources/request.json");

        props.setProperty("merchantKeyId", "08c94330-f618-42a3-b09d-e1e43be5efda");
        props.setProperty("merchantsecretKey", "yBJxy6LjM2TmcPGu+GaJrHtkke25fPpUX+UY6/L/1tE=");

        merchantConfig = new MerchantConfig(props);
    }

	@Mock
	KeyCertificateGenerator keyCertificateGenerator;

	@Rule
	public ExpectedException thrown = ExpectedException.none();


	@Test
	public void initializeCertificateTestPass()
			throws CertificateException, NoSuchAlgorithmException, KeyStoreException, Exception {

		merchantConfig.validateMerchantDetails("GET");

		keyCertificateGenerator = new KeyCertificateGenerator();
		X509Certificate certificate = keyCertificateGenerator.initializeCertificateOfMerchantForJWT(merchantConfig);
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(certificate);
		} else {
			Assert.assertNotNull(certificate);
		}
	}

	@Test
	public void initializePrivateKeyTestPass()
			throws CertificateException, NoSuchAlgorithmException, KeyStoreException, Exception {

		merchantConfig.validateMerchantDetails("GET");

		keyCertificateGenerator = new KeyCertificateGenerator();
		RSAPrivateKey privateKey = keyCertificateGenerator.initializePrivateKey(merchantConfig);
		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			Assert.assertNull(privateKey);
		} else {
			Assert.assertNotNull(privateKey);
		}
	}
}
