package com.cybersource.authsdk.util;

import java.util.StringTokenizer;

public class Utility {
	private enum validationType {
		HTTP_SIGNATURE, JWT
	};

	/**
	 * Checks whether the authentication type is rightly entered with no typo
	 * error.
	 * 
	 * @param authenticationType
	 *            : HTTP or JWT
	 * @return -true if valid authentication type else return false.
	 */
	public static boolean checkAuthenticationValidation(String authenticationType) {
		boolean status = false;

		if (authenticationType.equalsIgnoreCase(validationType.HTTP_SIGNATURE.toString())
				|| authenticationType.equalsIgnoreCase(validationType.JWT.toString())) {
			status = true;
		}
		return status;
	}

	/**
	 * 
	 * @param requestTarget
	 *            : request target from where we will retrieve Id
	 * @return id from request target.
	 */
	public static String retrieveGetIDFromRequestTarget(String requestTarget) {
		StringTokenizer str = new StringTokenizer(requestTarget, "/");
		String temp = null;
		while (str.hasMoreTokens()) {
			temp = str.nextToken();
			if (temp.matches("^[0-9]+$")) {
				return temp;
			}
		}
		return temp;
	}
}
