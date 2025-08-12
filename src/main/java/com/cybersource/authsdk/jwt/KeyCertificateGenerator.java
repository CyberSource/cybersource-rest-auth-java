package com.cybersource.authsdk.jwt;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.mle.MLEException;

public class KeyCertificateGenerator {

	/**
	 * @param merchantConfig -contains merchant information.
	 * @return certificate which will be used to generate token.
	 ** @throws CertificateException     - if certificate is missing or wrong.
	 * @throws NoSuchAlgorithmException - if algorithm is not available.
	 * @throws IOException              - if file is not found.
	 * @throws KeyStoreException        - if file is not available in key store or
	 *                                  wrong.
	 * @throws ConfigException          - if some values is missing or wrong for
	 *                                  merchant.
	 * @throws Exception                - if some other exception will happen.
	 */
	public X509Certificate initializeCertificateOfMerchantForJWT(MerchantConfig merchantConfig) throws CertificateException,
	NoSuchAlgorithmException, IOException, KeyStoreException, ConfigException, Exception {
		if (merchantConfig != null && merchantConfig.getKeyAlias() != null && merchantConfig.getKeyFile() != null) {
			KeyStore merchantKeyStore = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
			merchantKeyStore.load(new FileInputStream(merchantConfig.getKeyFile()),
					merchantConfig.getKeyPass().toCharArray());

			String merchantKeyAlias = null;
			Enumeration<String> enumKeyStore = merchantKeyStore.aliases();
			ArrayList<String> array = new ArrayList<String>();

			while (enumKeyStore.hasMoreElements()) {

				merchantKeyAlias = (String) enumKeyStore.nextElement();
				array.add(merchantKeyAlias);

			}
			merchantKeyAlias = keyAliasValidator(array,merchantConfig.getKeyAlias());

			try {
				PrivateKeyEntry e = (PrivateKeyEntry) merchantKeyStore.getEntry(merchantKeyAlias,
						new PasswordProtection(merchantConfig.getKeyPass().toCharArray()));
				return (X509Certificate) e.getCertificate();
			} catch (UnrecoverableEntryException var5) {
				return null;
			}

		} else {
			return null;
		}
	}
	
	/**
	 * @param merchantConfig -contains merchant information.
	 * @return certificate which will be used for MLE to encrypt request payload.
	 ** @throws CertificateException     - if certificate is missing or wrong.
	 * @throws NoSuchAlgorithmException - if algorithm is not available.
	 * @throws IOException              - if file is not found.
	 * @throws KeyStoreException        - if file is not available in key store or
	 *                                  wrong.
	 * @throws ConfigException          - if some values is missing or wrong for
	 *                                  merchant.
	 * @throws Exception                - if some other exception will happen.
	 */
	public X509Certificate initializeMLECertificate(MerchantConfig merchantConfig) throws CertificateException,
	NoSuchAlgorithmException, IOException, KeyStoreException, ConfigException, Exception {
		if (merchantConfig != null && merchantConfig.getRequestMleKeyAlias() != null && merchantConfig.getKeyFile() != null) {
			KeyStore merchantKeyStore = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
			merchantKeyStore.load(new FileInputStream(merchantConfig.getKeyFile()),
					merchantConfig.getKeyPass().toCharArray());

			String merchantKeyAlias = null;
			Enumeration<String> enumKeyStore = merchantKeyStore.aliases();
			ArrayList<String> array = new ArrayList<String>();

			while (enumKeyStore.hasMoreElements()) {

				merchantKeyAlias = (String) enumKeyStore.nextElement();
				array.add(merchantKeyAlias);

			}
			merchantKeyAlias = keyAliasValidator(array,merchantConfig.getRequestMleKeyAlias());

			try {
				X509Certificate certificate = (X509Certificate) merchantKeyStore.getCertificate(merchantKeyAlias);
				return certificate;
			} catch (Exception var5) {
				throw new MLEException("No certificate found for Request MLE for given requestMleKeyAlias "+  merchantConfig.getRequestMleKeyAlias() + " in p12 file " + merchantConfig.getKeyFilename() + ".p12");
			}

		} else {
			return null;
		}
	}
	
	
	/**
	 * @param array      -list of keyAlias.
	 * @param keyAlias -Id of merchant.
	 * @return merchantKeyalias for merchant.
	 */
	private static String keyAliasValidator(ArrayList<String> array, String keyAlias) {
		int size = array.size();
		String tempKeyAlias, merchantKeyAlias, result;
		StringTokenizer str;
		for (int i = 0; i < size; i++) {
			merchantKeyAlias = array.get(i).toString();
			str = new StringTokenizer(merchantKeyAlias, ",");
			while (str.hasMoreTokens()) {
				tempKeyAlias = str.nextToken();
				if (tempKeyAlias.contains("CN")) {
					str = new StringTokenizer(tempKeyAlias, "=");
					while (str.hasMoreElements()) {
						result = str.nextToken();
						if (result.equalsIgnoreCase(keyAlias)) {
							return merchantKeyAlias;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param merchantConfig - contains merchant information.
	 * @return certificate which will be used to generate token.
	 * @throws CertificateException     if certificate is missing or wrong.
	 * @throws NoSuchAlgorithmException if algorithm is not available.
	 * @throws IOException              if file is not found.
	 * @throws KeyStoreException        if file is not available in key store or
	 *                                  wrong.
	 * @throws ConfigException          if some values is missing or wrong for
	 *                                  merchant.
	 * @throws Exception                if some other exception will happen.
	 */
	public RSAPrivateKey initializePrivateKey(MerchantConfig merchantConfig) throws CertificateException,
			NoSuchAlgorithmException, IOException, KeyStoreException, ConfigException, Exception {
		if (merchantConfig != null && merchantConfig.getKeyAlias() != null && merchantConfig.getKeyFile() != null) {
			KeyStore merchantKeyStore = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
			merchantKeyStore.load(new FileInputStream(merchantConfig.getKeyFile()),
					merchantConfig.getKeyPass().toCharArray());
			String merchantKeyAlias = null;
			Enumeration<String> enumKeyStore = merchantKeyStore.aliases();

			while (enumKeyStore.hasMoreElements()) {
				merchantKeyAlias = (String) enumKeyStore.nextElement();
				if (merchantKeyAlias.contains(merchantConfig.getKeyAlias())) {
					break;
				}
			}

			try {
				PrivateKeyEntry e = (PrivateKeyEntry) merchantKeyStore.getEntry(merchantKeyAlias,
						new PasswordProtection(merchantConfig.getKeyPass().toCharArray()));
				return (RSAPrivateKey) e.getPrivateKey();
			} catch (UnrecoverableEntryException var5) {
				return null;
			}
		} else {
			return null;
		}
	}
}
