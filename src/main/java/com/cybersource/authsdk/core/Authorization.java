package com.cybersource.authsdk.core;

import com.cybersource.authsdk.http.HttpSignatureToken;
import com.cybersource.authsdk.jwt.JwtSignatureToken;
import com.cybersource.authsdk.util.GlobalLabelParameters;

import java.util.Objects;

/**
 * This class calls for the generation of Signature message depending on the
 * authentication type.
 */
public class Authorization {
	private String jwtRequestBody = null;

	/**
	 * @param jwtrequestBody - not null and required values for JWT
	 */
	public void setJWTRequestBody(String jwtrequestBody) {
		this.jwtRequestBody = jwtrequestBody;
	}

	/**
	 * @param merchantConfig -contains merchant information.
	 * @return token generated as per http_signature or JWT.
	 * @throws ConfigException - if some value will be wrong or missing for
	 *                         merchant.
	 */
	public String getToken(MerchantConfig merchantConfig, String method, String requestData) throws ConfigException, NullPointerException {
		String authType = merchantConfig.getAuthenticationType();
		if (authType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			 return Objects.requireNonNull(TokenGeneratorFactory.getAuthToken(merchantConfig, jwtRequestBody, method)).getToken(requestData);
		} else if (authType.equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			return Objects.requireNonNull(TokenGeneratorFactory.getAuthToken(merchantConfig, jwtRequestBody, method)).getToken();
		} else if (authType.equalsIgnoreCase(GlobalLabelParameters.OAUTH)) {
			return Objects.requireNonNull(TokenGeneratorFactory.getAuthToken(merchantConfig, jwtRequestBody, method)).getToken();
		}else{
			throw new ConfigException("Unsupported authentication type: " + authType);
		}
	}
}
