package com.gmail.rgizmalkov.dev.impl;

import com.gmail.rgizmalkov.dev.ival.Status;
import com.gmail.rgizmalkov.dev.ival.ValidationResponse;
import com.gmail.rgizmalkov.dev.ival.Validator;

public class NotNullValidator implements Validator{
    private boolean isPassed;

    @Override
    public boolean passed() {
        return isPassed;
    }

    @Override
    public ValidationResponse validation(Object o) {
        this.isPassed = o == null;

        return new ValidationResponse("Null validation", this.isPassed ? "success" : "error", this.isPassed ? Status.OK : Status.FAILED);
    }
}
