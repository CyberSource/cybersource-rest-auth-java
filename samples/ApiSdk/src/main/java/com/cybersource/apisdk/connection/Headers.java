package com.cybersource.apisdk.connection;

import java.util.Map;

import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.SignatureGenerationException;

/**
 * This interface will be used to get headers as per JWT and HTTP.
 */
public interface Headers {
	/**
	 * Setting of merchantID i.e the Header attribute of the token, into the
	 * connection object.
	 * 
	 * @throws ConfigException
	 *             - throws if some value is missing for merchant.
	 * @throws ClientException
	 *             - throws custom client exception .
	 * @throws SignatureGenerationException
	 *             - throws custom signature exception .
	 * @return headerMap
	 */
	public Map<String, String> setHeaderData() throws ConfigException, ClientException, SignatureGenerationException;

}
