package com.cybersource.authsdk.core;

import org.apache.logging.log4j.Logger;
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

	/**
	 * @param jwtrequestBody
	 */
	public void setJWTRequestBody(String jwtrequestBody) {
		this.jwtRequestBody = jwtrequestBody;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	
	/**
	 * @param merchantConfig
	 * @return
	 * @throws Exception
	 */
	public String getToken(MerchantConfig merchantConfig) throws ConfigException {
		return TokenGeneratorFactory.getAuthToken(merchantConfig, jwtRequestBody).getToken();

	}

}
