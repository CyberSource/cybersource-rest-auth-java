package com.cybersource.authsdk.util.mle;

public class MLEException extends Exception {
    public MLEException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public MLEException(String message) {
        super(message);
    }
}
