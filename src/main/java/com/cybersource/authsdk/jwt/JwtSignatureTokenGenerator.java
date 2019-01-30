package com.cybersource.authsdk.jwt;

import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.cache.Cache;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.TokenGenerator;
import com.cybersource.authsdk.jwtsecurity.JWTCryptoProcessorImpl;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.MerchantUtil;
import com.cybersource.authsdk.util.Utility;

/**
 * This class will be used to generate JWT token.
 *
 */
public class JwtSignatureTokenGenerator implements TokenGenerator {
	private MerchantConfig merchantConfig;
	private String requestBody;
	private String requestType;
	private Logger logger;
	private Cache cache;
	private RSAPrivateKey rsaPrivateKey = null;
	private X509Certificate x509Certificate = null;
	private String signature;

	/**
	 * @param merchantConfig
	 *            - list of parameters required for JWT token.
	 * @param requestData
	 *            -request data to be sent.
	 * @param requestType
	 *            -request type to be sent. 
	 */
	public JwtSignatureTokenGenerator(MerchantConfig merchantConfig, String requestData) {

		this.merchantConfig = merchantConfig;
		this.requestBody = requestData;
		this.requestType = MerchantUtil.getRequestType();
		this.logger = merchantConfig.getLogger();
		this.cache = new Cache(this.merchantConfig);
	}

	@Override
	public String getToken() throws ConfigException {
		try {
			signature = generateSignature();
		} catch (Exception e) {
			signature = null;
			throw new ConfigException(GlobalLabelParameters.JWT_SIG_FAILED);
		}
		return signature;
	}

	/**
	 * 
	 * @return the generated signature.
	 * @throws ConfigException
	 *             - if some value is missing for merchant.
	 * @throws Exception
	 *             - if some exception will occur.
	 */
	public String generateSignature() throws ConfigException {

		try {
			if (merchantConfig != null && merchantConfig.getKeyType() != null) {
				/* Generating private key. */
				if (Cache.isCache) {
					rsaPrivateKey = KeyCertificateGenerator.initializePrivateKey(merchantConfig);
					x509Certificate = KeyCertificateGenerator.initializeCertificate(merchantConfig);
					setUpCache();
					Cache.isCache = false;
				} else {
					boolean lastModified = cache.isLastModifiedTimeP12();
					if (lastModified) {
						cache.retriveP12DataFromCache();
						rsaPrivateKey = cache.getRsaPrivateKey();
						x509Certificate = cache.getX509Certificate();
					} else { 
						rsaPrivateKey = KeyCertificateGenerator.initializePrivateKey(merchantConfig);
						x509Certificate = KeyCertificateGenerator.initializeCertificate(merchantConfig);
						setUpCache();
					}
				}
				/* Generating certificate. */

				String encryptedSigMessage = "";

				if (requestBody != null && !requestBody.isEmpty()) {
					MessageDigest jwtBody = MessageDigest.getInstance(GlobalLabelParameters.SHA_256);
					byte[] jwtClaimSetBody = jwtBody.digest(requestBody.getBytes());
					encryptedSigMessage = Base64.getEncoder().encodeToString(jwtClaimSetBody);
				}

				String claimSet = null;
				if (requestType.equalsIgnoreCase(GlobalLabelParameters.GET)
						|| requestType.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
					claimSet = "{\n            \"iat\":\"" + ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT)
							+ "\"\n} \n\n";
				} else if (requestType.equalsIgnoreCase(GlobalLabelParameters.POST)
						|| requestType.equalsIgnoreCase(GlobalLabelParameters.PUT)
						|| requestType.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
					claimSet = "{\n            \"digest\":\"" + encryptedSigMessage
							+ "\",\n            \"digestAlgorithm\":\"SHA-256\",\n            \"iat\":\""
							+ ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT) + "\"\n} \n\n";

				}
				/* Preparing the Signature Header. */
				HashMap<String, Object> customHeaders = new HashMap<String, Object>();
				if (merchantConfig.getMerchantID() != null) {
					customHeaders.put(JWTCryptoProcessorImpl.MERCHANT_ID, merchantConfig.getMerchantID());
				}

				if (merchantConfig.getMerchantID() != null) {
					customHeaders.put("v-c-partner-id", merchantConfig.getMerchantID());
				}

				String authType = merchantConfig.getAuthenticationType();
				JWTCryptoProcessorImpl jwtCryptoProcessor = new JWTCryptoProcessorImpl();

				if (GlobalLabelParameters.JWT.equalsIgnoreCase(authType)) {
					signature = jwtCryptoProcessor.sign(claimSet, rsaPrivateKey, x509Certificate, customHeaders);

				} else {
					if (!GlobalLabelParameters.JWE.equalsIgnoreCase(authType)) {
						throw new ConfigException("merchant config field is invalid: authType");
					}

					X509Certificate x509Cert = KeyCertificateGenerator.initializeRecipientCertificate(merchantConfig);
					signature = jwtCryptoProcessor.signAndEncrypt(claimSet, rsaPrivateKey, x509Certificate, x509Cert,
							customHeaders);
				}

			} else {
				throw new ConfigException("merchant config fields missing: keyType, key type");
			}
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.JWT_SIG_FAILED, "", Level.FATAL);
			signature = null;
			throw new ConfigException(GlobalLabelParameters.JWT_SIG_FAILED);
		}
		return signature;
	}

	/**
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	private void setUpCache() throws ConfigException {
		cache.setX509Certificate(x509Certificate);
		cache.setRsaPrivateKey(rsaPrivateKey);
		cache.setP12FileDetailsInCache();
	}
}
