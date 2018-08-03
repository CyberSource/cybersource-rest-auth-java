package com.cybersource.apisdk.connection;

/**
 * 
 * 
 *
 */
public interface Connection {

	/**
	 * Depending on the URL, connection is opened to the API server.
	 */
	void openConnection();

	/**
	 * After successfully operation, connection is nullified.
	 */
	void endConnection();

	/**
	 * 
	 */
	void setProxyConnection();
}
