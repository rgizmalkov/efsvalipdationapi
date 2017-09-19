package com.gmail.rgizmalkov.dev.meta;

import efs.validation.core.meta.DynamicManagedValue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@DynamicManagedValue(priority = 2)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Set {
    String value();
}
