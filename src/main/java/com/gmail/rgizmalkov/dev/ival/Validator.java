package com.gmail.rgizmalkov.dev.ival;

public interface Validator {

    boolean passed();

    ValidationResponse validation(Object o);

}
