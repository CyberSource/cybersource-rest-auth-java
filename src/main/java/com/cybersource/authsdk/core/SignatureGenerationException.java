package com.cybersource.authsdk.core;


public class SignatureGenerationException extends RuntimeException{
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