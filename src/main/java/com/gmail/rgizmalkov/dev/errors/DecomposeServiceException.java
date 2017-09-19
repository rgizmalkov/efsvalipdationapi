package com.gmail.rgizmalkov.dev.errors;

public class DecomposeServiceException extends RuntimeException
{
    public DecomposeServiceException() {
    }

    public DecomposeServiceException(String message) {
        super(message);
    }

    public DecomposeServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecomposeServiceException(Throwable cause) {
        super(cause);
    }

    public DecomposeServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
