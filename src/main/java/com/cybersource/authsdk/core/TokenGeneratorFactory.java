package com.cybersource.authsdk.core;

import com.cybersource.authsdk.http.HttpSignatureTokenGenerator;
import com.cybersource.authsdk.jwt.JwtSignatureTokenGenerator;
import com.cybersource.authsdk.util.GlobalLabelParameters;

/**
 * This class calls for the generation of Token depending on the authentication
 * type.
 */
public class TokenGeneratorFactory {

	/**
	 * @param requestType 
	 * @param merchantConfig-
	 *            contains information of merchant.
	 * @param requestBody-
	 *            request data.
	 * @return token as per http_signature and JWT.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public static TokenGenerator getAuthTokenGenerator(MerchantConfig merchantConfig, String requestBody)
			throws ConfigException {
		String authType = merchantConfig.getAuthenticationType();

		if (authType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			return new HttpSignatureTokenGenerator(merchantConfig, requestBody);
		} else if (authType.equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			return new JwtSignatureTokenGenerator(merchantConfig, requestBody);
		} else {
			throw new ConfigException(GlobalLabelParameters.AUTH_ERROR);
		}
	}
}