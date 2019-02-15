package com.cybersource.authsdk.core;

import com.cybersource.authsdk.util.Utility;

/**
 * This interface calls for the generation of Token depending on the
 * authentication type.
 */
public interface TokenGenerator {

	/**
	 * @return token generated as per http_signature or JWT
	 * @throws ConfigException
	 *            - if some value is missing for merchant.
	 * @throws SignatureGenerationException 
	 *            - throws custom signature generation exception.
	 */
	String getToken() throws ConfigException, SignatureGenerationException;

	default void validateGivenInput(MerchantConfig config, RequestContext context) throws ConfigException {
		if(config != null && context != null){
			Utility.isRequestMethodTypeValid(context.getRequestMethodType());
		}else if(config == null){
			throw new ConfigException("Merchant Config object is null");
		}else{
			throw new ConfigException("RequestContext object is null");
		}
	}
}
