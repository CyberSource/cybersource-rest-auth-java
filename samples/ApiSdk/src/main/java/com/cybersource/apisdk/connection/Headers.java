package com.cybersource.apisdk.connection;

public interface Headers {

	/**
	 * Encrypted message of requestJson is added to the connection object..
	 * 
	 */
	void setDigest();

	/**
	 * Setting of merchantID i.e the Header attribute of the token, into the
	 * connection object.
	 * 
	 * @param merchantId
	 *            : The unique name of the merchant.
	 */
	void setHeaderData(String merchantId);

	/**
	 * Input type of digest is set into the connection object.
	 */
	void setContentType();

	/**
	 * The response message obtained form the server is sent for masking the
	 * important fields.
	 */
	void maskingPayload(String serverResponse);

	/**
	 * 
	 */
	void setUserAgent();

	/**
	 * Generation of encrypted signature is obtained and set into the connection
	 * object.
	 */
	void setSignature();

	/**
	 * Sets the request type ( GET/POST )
	 */
	void setRequestMethod(String method);

}
