package com.cybersource.authsdk.core;

import com.cybersource.authsdk.http.HttpSignatureToken;
import com.cybersource.authsdk.jwt.JwtSignatureToken;
import com.cybersource.authsdk.oauth.OAuthToken;
import com.cybersource.authsdk.util.GlobalLabelParameters;

public class TokenGeneratorFactory {

	/**
	 * 
	 * @param merchantConfig
	 * @param requestBody
	 * @param reqVerb
	 * @param reqTarget
	 * @param date
	 * @return token - may be generated as HTTP signature or JWT
	 * @throws ConfigException if some value is missing or wrong for merchant
	 */
	public static TokenGenerator getAuthToken(MerchantConfig merchantConfig, String requestBody, String reqVerb, String reqTarget, String date)
			throws ConfigException {
		String authType = merchantConfig.getAuthenticationType();

		if (authType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			return new HttpSignatureToken(merchantConfig, requestBody, reqVerb, reqTarget, date);
		} else if (authType.equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			return new JwtSignatureToken(merchantConfig, requestBody, reqVerb);
		} else if (authType.equalsIgnoreCase(GlobalLabelParameters.OAUTH)) {
			return new OAuthToken(merchantConfig);
		} else {
			return null;
		}
	}
}