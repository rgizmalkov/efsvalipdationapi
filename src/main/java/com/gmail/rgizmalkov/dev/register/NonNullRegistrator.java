package com.gmail.rgizmalkov.dev.register;

import com.gmail.rgizmalkov.dev.meta.NonNull;

public class NonNullRegistrator implements Registrator<NonNull> {

    @Override
    public Registration register(NonNull annotation) {
        return new Registration(annotation.criticality(), annotation.disable());
    }
}
