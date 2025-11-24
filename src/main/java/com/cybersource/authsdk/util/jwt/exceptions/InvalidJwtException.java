package com.cybersource.authsdk.util.jwt.exceptions;

public class InvalidJwtException extends Exception {
    public InvalidJwtException(String invalidJwtToken) {
        super(invalidJwtToken);
    }

    public InvalidJwtException(String invalidJwtToken, Throwable cause) {
        super(invalidJwtToken, cause);
    }
}
