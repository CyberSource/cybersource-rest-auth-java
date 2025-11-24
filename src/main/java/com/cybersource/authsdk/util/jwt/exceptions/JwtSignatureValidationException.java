package com.cybersource.authsdk.util.jwt.exceptions;

public class JwtSignatureValidationException extends Exception {
    public JwtSignatureValidationException(String message) {
        super(message);
    }

    public JwtSignatureValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
