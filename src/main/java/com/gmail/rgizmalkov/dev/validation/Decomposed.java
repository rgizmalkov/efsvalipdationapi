package com.gmail.rgizmalkov.dev.validation;

import java.lang.reflect.Field;
import java.util.List;

public interface Decomposed<O>{

    List<O> validationArray();

    O next();

    Field field();

    boolean isRootValidationObject();

}
