package com.gmail.rgizmalkov.dev.errors;

public class IterableDecomposeException extends Error {
    public IterableDecomposeException() {
    }

    public IterableDecomposeException(String message) {
        super(message);
    }

    public IterableDecomposeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IterableDecomposeException(Throwable cause) {
        super(cause);
    }

    public IterableDecomposeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
