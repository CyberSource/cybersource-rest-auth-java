package com.cybersource.authsdk.core;

import com.cybersource.authsdk.util.GlobalLabelParameters;

import java.util.Objects;

/**
 * This class calls for the generation of Signature message depending on the
 * authentication type.
 */
public class Authorization {
	/**
	 * Generates authentication token based on merchant configuration and request parameters.
	 * 
	 * @param merchantConfig contains merchant information
	 * @param reqVerb the HTTP request method (GET, POST, PUT, etc.)
	 * @param requestData the request payload data
	 * @param reqTarget the request target URL path
	 * @param date the date string for the request
	 * @return token generated as per http_signature or JWT.
	 * @throws ConfigException if some value will be wrong or missing for
	 *   merchant.
	 */
	public String getToken(MerchantConfig merchantConfig, String reqVerb, String requestData, String reqTarget, String date, boolean isResponseMLEForApi) throws ConfigException {
		return TokenGeneratorFactory.getAuthToken(merchantConfig, requestData, reqVerb, reqTarget, date, isResponseMLEForApi).getToken();
	}
}
