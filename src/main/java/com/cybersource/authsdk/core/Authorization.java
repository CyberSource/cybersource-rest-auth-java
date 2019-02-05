package com.cybersource.authsdk.core;

import com.cybersource.authsdk.util.AuthenticationRequestContext;

/**
 * This class calls for the generation of Signature message depending on the
 * authentication type.
 */
public class Authorization {

	/**
	 * @param merchantConfig
	 *            -contains merchant information.
	 * @param requestContext
	 *            - contains request details
	 * @return token generated as per http_signature or JWT.
	 * @throws ConfigException
	 *             - if some value will be wrong or missing for merchant.
	 */
	public static String getToken(MerchantConfig merchantConfig, AuthenticationRequestContext authRequestContext) throws ConfigException {
		return TokenGeneratorFactory.getAuthTokenGenerator(merchantConfig, authRequestContext).getToken();
	}
}
