package com.cybersource.authsdk.core;

import com.cybersource.authsdk.util.GlobalLabelParameters;

import java.util.Objects;

/**
 * This class calls for the generation of Signature message depending on the
 * authentication type.
 */
public class Authorization {

	/**
	 * @param merchantConfig -contains merchant information.
	 * @return token generated as per http_signature or JWT.
	 * @throws ConfigException - if some value will be wrong or missing for
	 *                         merchant.
	 */
	public String getToken(MerchantConfig merchantConfig, String reqVerb, String requestData, String reqTarget, String date) throws ConfigException {
		return TokenGeneratorFactory.getAuthToken(merchantConfig, requestData, reqVerb, reqTarget, date).getToken();
	}
}
