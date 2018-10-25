package com.cybersource.authsdk.core;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.TokenGenerator;
import com.cybersource.authsdk.http.HttpSignatureToken;
import com.cybersource.authsdk.jwt.JwtSignatureToken;
import com.cybersource.authsdk.util.GlobalLabelParameters;

public class TokenGeneratorFactory {

	/**
	 * @param merchantConfig
	 * @param jwtRequestBody
	 * @return
	 * @throws Exception
	 */
	public static TokenGenerator getAuthToken(MerchantConfig merchantConfig, String jwtRequestBody)
			throws ConfigException {
		String authType = merchantConfig.getAuthenticationType();

		if (authType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			return new HttpSignatureToken(merchantConfig);
		} else if (authType.equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			return new JwtSignatureToken(merchantConfig, jwtRequestBody);
		} else {
			return null;
		}
	}
}