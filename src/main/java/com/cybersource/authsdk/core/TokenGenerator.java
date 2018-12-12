package com.cybersource.authsdk.core;
/**
 * This interface calls for the generation of Token depending on the
 * authentication type.
 */
public interface TokenGenerator {

	/**
	 * @return token generated as per http_signature or JWT
	 * @throws ConfigException
	 *             - if some value is missing for merchant.
	 */
	public String getToken() throws ConfigException;

}
