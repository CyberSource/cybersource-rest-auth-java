package com.cybersource.authsdk.core;

public interface TokenGenerator {
	/**
	 * @return token generated<p>
	 * Token may be generated as HTTP signature or JWT
	 */
	public String getToken();

}
