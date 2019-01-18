package com.cybersource.apisdk.service;

/**
 * This class will throw specific message as per exception.
 */
public class ClientException extends Exception {
	private static final long serialVersionUID = -7810688045599318234L;

	/**
	 * @param message
	 *            - message to print if exception occurs
	 */
	public ClientException(String message) {
		super(message);
	}
}
