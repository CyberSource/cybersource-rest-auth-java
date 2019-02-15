package com.cybersource.apisdk.connection;

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
	 * @throws ConfigException
	 *             - throws if some value is missing for merchant.
	 * @throws ClientException
	 *             - throws custom client exception .
	 * @throws SignatureGenerationException 
	 *            - throws custom signature exception .
	 */
	void setHeaderData() throws ConfigException, ClientException, SignatureGenerationException;

	/**
	 * The response message obtained form the server is sent for masking the
	 * important fields.
	 * @param serverResponse
	 * @param title
	 */
	void maskingPayload(String serverResponse, String title);

	/**
	 * Sets the request type ( GET/POST )
	 * @param method type
	 * @throws ClientException
	 *             - throws custom client exception .
	 */
	void setRequestMethod(String method) throws ClientException;
}
