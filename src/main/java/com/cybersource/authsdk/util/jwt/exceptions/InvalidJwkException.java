package com.cybersource.authsdk.util.jwt.exceptions;

public class InvalidJwkException extends Exception {
    public InvalidJwkException(String message) {
        super(message);
    }

    public InvalidJwkException(String message, Throwable cause) {
        super(message, cause);
    }
}
