package com.gmail.rgizmalkov.dev.meta;

import com.gmail.rgizmalkov.dev.ival.Format;
import efs.validation.core.meta.DynamicManagedValue;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//@DynamicManagedValue(priority = 1)
@Retention(RetentionPolicy.RUNTIME)
public @interface Default {
    String key();
    Format format() default Format.JSON;
}
