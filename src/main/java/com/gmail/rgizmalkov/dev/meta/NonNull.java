package com.gmail.rgizmalkov.dev.meta;

import com.gmail.rgizmalkov.dev.impl.NotNullValidator;
import efs.validation.core.meta.RelatedValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Validation
@RelatedValidator(NotNullValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonNull {
    String criticality() default "validation.api.nonnull.criticality";
    String disable() default "validation.api.nonnull.disable";
}
