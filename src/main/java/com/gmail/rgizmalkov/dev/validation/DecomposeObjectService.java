package com.gmail.rgizmalkov.dev.validation;

public interface DecomposeObjectService<O> {
    Decomposed<O> decompose(Object o);

}
