package com.cybersource.apisdk.connection;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.cybersource.apisdk.service.ClientException;
import com.cybersource.authsdk.core.ConfigException;

/**
 * This interface will be used to get headers as per JWT and HTTP.
 */
public interface Headers {
	/**
	 * Setting of merchantID i.e the Header attribute of the token, into the
	 * connection object.
	 * 
	 * @param merchantId
	 *            : The unique name of the merchant.
	 * @throws ConfigException
	 *             - if some value is missing for merchant.
	 * @throws ClientException
	 *             - throws custom client exception .
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	void setHeaderData()
			throws ConfigException, ClientException, NoSuchAlgorithmException, UnsupportedEncodingException;

	/**
	 * The response message obtained form the server is sent for masking the
	 * important fields.
	 */
	void maskingPayload(String serverResponse);

	/**
	 * Sets the request type ( GET/POST )
	 * 
	 * @throws ClientException
	 *             - throws custom client exception .
	 */
	void setRequestMethod(String method) throws ClientException;
}
