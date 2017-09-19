package com.gmail.rgizmalkov.dev.meta;

import com.gmail.rgizmalkov.dev.impl.NotNullValidator;
import efs.validation.core.meta.RelatedValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Validation
@RelatedValidator(NotNullValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Pattern {
    String value();
}
