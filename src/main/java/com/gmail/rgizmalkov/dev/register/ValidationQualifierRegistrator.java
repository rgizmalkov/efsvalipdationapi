package com.gmail.rgizmalkov.dev.register;

import com.gmail.rgizmalkov.dev.meta.ValidationQualifier;

public class ValidationQualifierRegistrator implements Registrator<ValidationQualifier> {
    @Override
    public Registration register(ValidationQualifier annotation) {
        return new Registration(annotation.criticality(), annotation.disable());
    }
}
