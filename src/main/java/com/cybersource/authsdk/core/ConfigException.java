package com.cybersource.authsdk.core;

/**
 * This class will throw specific message as per exception.
 */
public class ConfigException extends Exception {
	private static final long serialVersionUID = -7810688045599318234L;

	/**
	 * @param message
	 *            - messgae to print if exception occurs
	 */
	public ConfigException(String message) {
		super(message);
	}
    
	/**
	 * @param message
	 *            - messgae to print if exception occurs
	 * @param throwable
	 *            - passing Throwable
	 */
	public ConfigException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
