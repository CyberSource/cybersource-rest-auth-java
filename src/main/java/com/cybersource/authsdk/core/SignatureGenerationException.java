package com.cybersource.authsdk.core;


public class SignatureGenerationException extends Exception {
    /**
	 * This class will throw signature related specific message as per exception.
	 */
	private static final long serialVersionUID = 5923788812181927433L;

	public SignatureGenerationException() {
    }

    public SignatureGenerationException(String message) {
        super(message);
    }

    public SignatureGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignatureGenerationException(Throwable cause) {
        super(cause);
    }

    public SignatureGenerationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}