package com.cybersource.authsdk.core;

import com.cybersource.authsdk.http.HttpSignatureToken;
import com.cybersource.authsdk.jwt.JwtSignatureToken;
import com.cybersource.authsdk.oauth.OAuthToken;
import com.cybersource.authsdk.util.GlobalLabelParameters;

public class TokenGeneratorFactory {

	/**
	 * @param merchantConfig - merchant configuration information
	 * @param jwtRequestBody - request data to be sent
	 * @return token - may be generated as HTTP signature or JWT
	 * @throws ConfigException if some value is missing or wrong for merchant
	 */
	public static TokenGenerator getAuthToken(MerchantConfig merchantConfig, String jwtRequestBody, String method)
			throws ConfigException {
		String authType = merchantConfig.getAuthenticationType();

		if (authType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			return new HttpSignatureToken(merchantConfig, method);
		} else if (authType.equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			return new JwtSignatureToken(merchantConfig, jwtRequestBody);
		} else if (authType.equalsIgnoreCase(GlobalLabelParameters.OAUTH)) {
			return new OAuthToken(merchantConfig);
		} else {
			return null;
		}
	} //
}