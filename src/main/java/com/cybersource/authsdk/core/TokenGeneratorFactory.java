package com.cybersource.authsdk.core;

import com.cybersource.authsdk.http.HttpSignatureToken;
import com.cybersource.authsdk.jwt.JwtSignatureToken;
import com.cybersource.authsdk.oauth.OAuthToken;
import com.cybersource.authsdk.util.GlobalLabelParameters;

public class TokenGeneratorFactory {

	/**
	 * Creates and returns an authentication token generator based on merchant configuration.
	 * 
	 * @param merchantConfig the merchant configuration containing authentication details
	 * @param requestBody the request payload body
	 * @param reqVerb the HTTP request method (GET, POST, PUT, etc.)
	 * @param reqTarget the request target URL path
	 * @param date the date string for the request
	 * @return token - may be generated as HTTP signature or JWT
	 * @throws ConfigException if some value is missing or wrong for merchant
	 */
	public static TokenGenerator getAuthToken(MerchantConfig merchantConfig, String requestBody, String reqVerb, String reqTarget, String date, boolean isResponseMLEForApi)
			throws ConfigException {
		String authType = merchantConfig.getAuthenticationType();

		if (authType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			return new HttpSignatureToken(merchantConfig, requestBody, reqVerb, reqTarget, date);
		} else if (authType.equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			return new JwtSignatureToken(merchantConfig, requestBody, reqVerb, reqTarget, isResponseMLEForApi);
		} else if (authType.equalsIgnoreCase(GlobalLabelParameters.OAUTH)) {
			return new OAuthToken(merchantConfig);
		} else {
			return null;
		}
	}
}