package com.cybersource.authsdk.jwt;

import java.security.MessageDigest;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.cache.Identity;
import com.cybersource.authsdk.cache.MerchantIdentityCache;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.core.TokenGenerator;
import com.cybersource.authsdk.jwtsecurity.JWTCryptoProcessorImpl;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

/**
 * This class will be used to generate JWT token.
 *
 */
public class JwtSignatureTokenGenerator implements TokenGenerator {
	private MerchantConfig merchantConfig;
	private RequestContext authRequestContext;
	private String jwtMethod;
	private Logger logger;

	/**
	 * @param merchantConfig
	 *            - list of parameters required for JWT token.
	 * @param authRequestContext
	 *            - contains request details
	 */
	public JwtSignatureTokenGenerator(MerchantConfig merchantConfig, RequestContext authRequestContext) {
		this.merchantConfig = merchantConfig;
		this.authRequestContext = authRequestContext;
		this.jwtMethod = authRequestContext.getRequestMethodType();
		this.logger = merchantConfig.getLogger();
	}

	@Override
	public String getToken() throws ConfigException {
		try {
			return generateSignature();
		} catch (ConfigException e) {
			throw e;
		} catch (Exception e) {
			throw new ConfigException(GlobalLabelParameters.SIG_FAILED, e);
		}
	}

	/**
	 * 
	 * @return the generated signature.
	 * @throws ConfigException
	 *             - if some value is missing for merchant.
	 */
	protected String generateSignature() throws ConfigException {
		try {
			if (merchantConfig != null) {
				String encryptedSigMessage;
				String claimSet = null;

				if (StringUtils.isNotBlank(jwtMethod)) {
					if (jwtMethod.equalsIgnoreCase(GlobalLabelParameters.GET)
							|| jwtMethod.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
						claimSet = "{\n            \"iat\":\""
								+ ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT) + "\"\n} \n\n";
					} else if (jwtMethod.equalsIgnoreCase(GlobalLabelParameters.POST)
							|| jwtMethod.equalsIgnoreCase(GlobalLabelParameters.PUT)
							|| jwtMethod.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {

						MessageDigest jwtBody = MessageDigest.getInstance(GlobalLabelParameters.SHA_256);
						byte[] jwtClaimSetBody = jwtBody.digest(authRequestContext.getRequestBody().getBytes());
						encryptedSigMessage = Base64.getEncoder().encodeToString(jwtClaimSetBody);

						claimSet = "{\n            \"digest\":\"" + encryptedSigMessage
								+ "\",\n            \"digestAlgorithm\":\"SHA-256\",\n            \"iat\":\""
								+ ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT) + "\"\n} \n\n";
					}
				}

				/* Preparing the Signature Header. */
				HashMap<String, Object> customHeaders = new HashMap<>();
				if (merchantConfig.getMerchantID() != null) {
					customHeaders.put(JWTCryptoProcessorImpl.MERCHANT_ID, merchantConfig.getMerchantID());
					customHeaders.put("v-c-partner-id", merchantConfig.getMerchantID());
				}

				String authType = merchantConfig.getAuthenticationType();
				JWTCryptoProcessorImpl jwtCryptoProcessor = new JWTCryptoProcessorImpl();
				Identity identity = MerchantIdentityCache.getInstance(merchantConfig.getLogger())
						.retrieveP12DataFromCache(merchantConfig);
				if (GlobalLabelParameters.JWT.equalsIgnoreCase(authType)) {
					return jwtCryptoProcessor.sign(claimSet, identity.getRsaPrivateKey(), identity.getX509(),
							customHeaders);

				} else {
					throw new ConfigException(GlobalLabelParameters.AUTH_ERROR);
				}

			} else {
				throw new ConfigException("merchant config fields missing: keyType, key type");
			}
		} catch (ConfigException e) {
			Utility.log(logger, e.getMessage(), "", Level.FATAL);
			throw e;
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.SIG_FAILED, "", Level.FATAL);
			throw new ConfigException(GlobalLabelParameters.SIG_FAILED, e);
		}
	}
}
