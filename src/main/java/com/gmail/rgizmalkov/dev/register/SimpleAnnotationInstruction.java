package com.gmail.rgizmalkov.dev.register;

import com.gmail.rgizmalkov.dev.ival.Status;

import java.lang.annotation.Annotation;

public class SimpleAnnotationInstruction implements AnnotationInstruction{

    Status status;
    boolean disable;

    public SimpleAnnotationInstruction(Status status, boolean disable) {
        this.status = status;
        this.disable = disable;
    }

    @Override
    public Status criticility() {
        return status;
    }

    @Override
    public boolean disable() {
        return disable;
    }
}
