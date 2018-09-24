package com.cybersource.authsdk.core;

import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.http.HttpSignatureToken;
import com.cybersource.authsdk.jwt.JwtSignatureToken;
import com.cybersource.authsdk.util.GlobalLabelParameters;

/**
 * This class calls for the generation of Signature message depending on the
 * authentication type.
 * 
 * 
 *
 */
public class Authorization {
	private Logger logger;
	private String jwtRequestBody = null;

	public void setJWTRequestBody(String jwtrequestBody) {
		this.jwtRequestBody = jwtrequestBody;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * This method generates and return a encrypted signature.
	 * 
	 * @param merchantConfig
	 * @param merchantKeyId
	 * @param merchantsecretKey
	 * @param merchantId
	 * @param requestType
	 * @param getID
	 * @return
	 * @throws Exception
	 */
	public String getToken(MerchantConfig merchantConfig, String merchantKeyId, String merchantSecretKey,
			String merchantId, String requestType, String getID) throws Exception {

		String authenticationType = merchantConfig.getAuthenticationType().trim();
		if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {

			HttpSignatureToken httpSigToken = new HttpSignatureToken(merchantConfig, merchantKeyId, merchantSecretKey,
					merchantId, requestType, getID);
			return httpSigToken.getToken();

		} else if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			JwtSignatureToken jwtSigToken = new JwtSignatureToken(merchantConfig, jwtRequestBody, logger, requestType);
			return jwtSigToken.getToken();
		}
		return null;
	}

}
