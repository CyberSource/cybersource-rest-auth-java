package com.cybersource.authsdk.jwt;

import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cybersource.authsdk.cache.Cache;
import com.cybersource.authsdk.cache.Identity;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.TokenGenerator;
import com.cybersource.authsdk.jwtsecurity.JWTCryptoProcessorImpl;
import com.cybersource.authsdk.util.GlobalLabelParameters;

import net.minidev.json.JSONObject;

import static com.cybersource.authsdk.util.mle.MLEUtility.validateAndAutoExtractResponseMleKid;

public class JwtSignatureToken implements TokenGenerator {
	private static Logger logger = LogManager.getLogger(JwtSignatureToken.class);
	
	private MerchantConfig merchantConfig;
	private String requestBody, jwtMethod;
	private Cache cache;
	private RSAPrivateKey rsaPrivateKey = null;
	private X509Certificate x509Certificate = null;
	private String signature;
	private boolean isResponseMLEForApi;
	private String httpResourcePath;

	/**
	 * Constructs a JWT signature token generator.
	 * 
	 * @param merchantConfig list of parameters required for JWT token
	 * @param requestBody user information to be sent
	 * @param reqVerb the HTTP request method
	 */
	public JwtSignatureToken(MerchantConfig merchantConfig, String requestBody, String reqVerb, String reqTarget, boolean isResponseMLEForApi) {

		this.merchantConfig = merchantConfig;
		this.requestBody = requestBody;
		this.jwtMethod = reqVerb;
		this.httpResourcePath = reqTarget;
		this.cache = new Cache();
		this.isResponseMLEForApi = isResponseMLEForApi;
	}

	@Override
	public String getToken() throws ConfigException {
		try {
			signature = generateSignature();
		} catch (ConfigException e) {
			signature = null;
			throw e;
		}
		return signature;
	}

	/**
	 * Creates a HashMap of custom header parameters that will be used to create the JWT
	 * @return HashMap of custom header parameters
	 */
	private HashMap<String, Object> getHeaderClaimSetAsHashMap() {
		HashMap<String, Object> headerClaimSet = new HashMap<>();

		// Registered header parameters under nimbus-jose-jwt are:
		// alg, jku, jwk, x5u, x5t, x5t#S256, x5c, kid, typ, cty, crit, b64

		// We are adding only `alg`, `kid` and `typ` as per security designs, in `JWTCryptoProcessorImpl.signPayload()`.
		// To add more registered header parameters, make changes to line in JWTCryptoProcessorImpl.java.

		// To add new custom headers not registered as part of nimbus-jose-jwt,
		// Add a line in this method with the following syntax:
		// headerClaimSet.put("header-name", header-value-object);

		return headerClaimSet;
	}

	/**
	 * Creates a JSONObject of claims for the payload for creating the JWT
	 * @param merchantConfig
	 * @param digest
	 * @return a JSONObject of claims for the payload
	 * @throws ConfigException
	 */
	private JSONObject getPayloadClaimSetAsJson(MerchantConfig merchantConfig, String digest) throws ConfigException {
		JSONObject payloadClaimSet = new JSONObject();

		String issuer;

		if (merchantConfig.isUseMetaKey()) {
			issuer = merchantConfig.getPortfolioID();
		} else {
			issuer = merchantConfig.getMerchantID();
		}

		if (this.jwtMethod.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| this.jwtMethod.equalsIgnoreCase(GlobalLabelParameters.PUT)
				|| this.jwtMethod.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
			payloadClaimSet.put("digest", digest);
			payloadClaimSet.put("digestAlgorithm", "SHA-256");
		}

		payloadClaimSet.put("iat", ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond());
		payloadClaimSet.put("exp", ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(2).toEpochSecond());

		payloadClaimSet.put("request-host", merchantConfig.getRunEnvironment());
		payloadClaimSet.put("request-resource-path", httpResourcePath);
		payloadClaimSet.put("request-method", this.jwtMethod);

		payloadClaimSet.put("iss", issuer);
		payloadClaimSet.put("jti", UUID.randomUUID().toString());
		payloadClaimSet.put("v-c-jwt-version", "2");
		if (merchantConfig.getMerchantID() != null) {
			payloadClaimSet.put(JWTCryptoProcessorImpl.MERCHANT_ID, merchantConfig.getMerchantID());
		}

		try {
			if(this.isResponseMLEForApi) {
				payloadClaimSet.put("v-c-response-mle-kid", validateAndAutoExtractResponseMleKid(merchantConfig));
			}
		} catch (Exception e) {
			logger.fatal(GlobalLabelParameters.JWT_SIG_FAILED);
			logger.error(e);
			signature = null;
			throw new ConfigException(GlobalLabelParameters.JWT_SIG_FAILED + " " + e.getMessage());
		}

		return payloadClaimSet;
	}

	/**
	 * 
	 * @return the generated signature.
	 * @throws ConfigException - if some exception will occur.
	 */
	public String generateSignature() throws ConfigException {
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

				String claimSet = getPayloadClaimSetAsJson(merchantConfig, encryptedSigMessage).toJSONString();
				HashMap<String, Object> customHeaders = getHeaderClaimSetAsHashMap();

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
			throw new ConfigException(GlobalLabelParameters.JWT_SIG_FAILED + " " + e.getMessage());
		}
		return signature;
	}

	private void getKeysForJWTFromCache() throws ConfigException, Exception {
		Identity p12Keys = cache.getMerchantP12KeysFromCache(this.merchantConfig);
		rsaPrivateKey = p12Keys.getRsaPrivateKey();
		x509Certificate = p12Keys.getX509();
	}

}
