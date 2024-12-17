package com.cybersource.authsdk.jwt;

import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cybersource.authsdk.cache.Cache;
import com.cybersource.authsdk.cache.Identity;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.TokenGenerator;
import com.cybersource.authsdk.jwtsecurity.JWTCryptoProcessorImpl;
import com.cybersource.authsdk.util.GlobalLabelParameters;

public class JwtSignatureToken implements TokenGenerator {
	private static Logger logger = LogManager.getLogger(JwtSignatureToken.class);
	
	private MerchantConfig merchantConfig;
	private String requestBody, jwtMethod;
	private Cache cache;
	private RSAPrivateKey rsaPrivateKey = null;
	private X509Certificate x509Certificate = null;
	private String signature;

	/**
	 * @param merchantConfig - list of parameters required for JWT token.
	 * @param requestBody    - user information to be sent.
	 */
//	public JwtSignatureToken(MerchantConfig merchantConfig, String requestBody) {
//
//		this.merchantConfig = merchantConfig;
//		this.requestBody = requestBody;
//		this.jwtMethod = merchantConfig.getRequestType();
//		this.cache = new Cache(this.merchantConfig);
//	}

	public JwtSignatureToken(MerchantConfig merchantConfig, String requestBody, String reqVerb) {

		this.merchantConfig = merchantConfig;
		this.requestBody = requestBody;
		this.jwtMethod = reqVerb;
		this.cache = new Cache(this.merchantConfig);
	}

//	@Override
//	public String getToken() {
//		try {
//			signature = generateSignature();
//		} catch (Exception e) {
//			signature = null;
//		}
//		return signature;
//	}

	@Override
	public String getToken(String requestData) {
		try {
			signature = generateSignature();
		} catch (Exception e) {
			signature = null;
		}
		return signature;
	}

	/**
	 * 
	 * @return the generated signature.
	 * @throws Exception - if some exception will occur.
	 */
	public String generateSignature() throws Exception {
		try {
			if (merchantConfig != null) {
				/* Generating private key. */
				getKeysForJWTFromCache();

				/* Generating certificate. */
				String encryptedSigMessage = "";

				if (requestBody != null && !requestBody.isEmpty()) {
					MessageDigest jwtBody = MessageDigest.getInstance(GlobalLabelParameters.SHA_256);
					byte[] jwtClaimSetBody = jwtBody.digest(requestBody.getBytes());
					encryptedSigMessage = Base64.getEncoder().encodeToString(jwtClaimSetBody);
				}

				String claimSet = null;
				String method = jwtMethod;
				if (method.equalsIgnoreCase(GlobalLabelParameters.GET)
						|| method.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
					claimSet = "{\n            \"iat\":\"" + ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT)
							+ "\"\n} \n\n";
				} else if (method.equalsIgnoreCase(GlobalLabelParameters.POST)
						|| method.equalsIgnoreCase(GlobalLabelParameters.PUT)
						|| method.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
					claimSet = "{\n            \"digest\":\"" + encryptedSigMessage
							+ "\",\n            \"digestAlgorithm\":\"SHA-256\",\n            \"iat\":\""
							+ ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT) + "\"\n} \n\n";

				}
				/* Preparing the Signature Header. */
				HashMap<String, Object> customHeaders = new HashMap<String, Object>();
				if (merchantConfig.getMerchantID() != null) {
					customHeaders.put(JWTCryptoProcessorImpl.MERCHANT_ID, merchantConfig.getMerchantID());
				}

				String authType = merchantConfig.getAuthenticationType().trim();
				JWTCryptoProcessorImpl jwtCryptoProcessor = new JWTCryptoProcessorImpl();

				if (GlobalLabelParameters.JWT.equalsIgnoreCase(authType)) {
					signature = jwtCryptoProcessor.sign(claimSet, rsaPrivateKey, x509Certificate, customHeaders);

				}

				/*
				 * JWE is not supported as an authentication mechanism
				 */
//			 	else {
//					if (!GlobalLabelParameters.JWE.equalsIgnoreCase(authType)) {
//						throw new ConfigException("merchant config field is invalid: authType");
//					}	
//				
//					X509Certificate x509Cert = KeyCertificateGenerator.initializeRecipientCertificate(merchantConfig);
//					signature = jwtCryptoProcessor.signAndEncrypt(claimSet, rsaPrivateKey, x509Certificate, x509Cert,
//							customHeaders);
//				}
			} else {
				throw new ConfigException("merchant config fields missing: keyType, key type");
			}
		} catch (Exception e) {
			logger.fatal(GlobalLabelParameters.JWT_SIG_FAILED);
			logger.error(e);
			signature = null;
		}
		return signature;
	}

	private void getKeysForJWTFromCache() throws ConfigException, Exception {
		Identity p12Keys = cache.getMerchantP12KeysFromCache();
		rsaPrivateKey = p12Keys.getRsaPrivateKey();
		x509Certificate = p12Keys.getX509();
	}

}
