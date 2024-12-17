package com.cybersource.authsdk.core;

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
	public String getToken(MerchantConfig merchantConfig, String method, String requestData) throws ConfigException {
		return TokenGeneratorFactory.getAuthToken(merchantConfig, jwtRequestBody, method).getToken(requestData);
	} //
}
