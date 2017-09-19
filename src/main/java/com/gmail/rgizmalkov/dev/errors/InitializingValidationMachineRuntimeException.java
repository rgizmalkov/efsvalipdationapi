package com.gmail.rgizmalkov.dev.errors;

public class InitializingValidationMachineRuntimeException extends RuntimeException {
    public InitializingValidationMachineRuntimeException() {
    }

    public InitializingValidationMachineRuntimeException(String message) {
        super(message);
    }

    public InitializingValidationMachineRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitializingValidationMachineRuntimeException(Throwable cause) {
        super(cause);
    }

    public InitializingValidationMachineRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
