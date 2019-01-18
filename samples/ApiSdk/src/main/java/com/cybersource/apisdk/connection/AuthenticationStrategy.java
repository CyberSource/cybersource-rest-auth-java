package com.cybersource.apisdk.connection;

import java.io.IOException;

import com.cybersource.apisdk.service.ClientException;

/**
 * This interface will be used to get common behavior for connection for JWT and
 * HTTP.
 */
public interface AuthenticationStrategy {

	/**
	 * Depending on the URL, connection is opened to the API server.
	 * 
	 * @throws ClientException
	 *             - throws custom client exception message.
	 * @throws IOException
	 *             - throws exception if some I/O error comes.
	 */
	void openConnection() throws ClientException;

	/**
	 * After successfully operation, connection is nullified.
	 */
	void endConnection();

}
