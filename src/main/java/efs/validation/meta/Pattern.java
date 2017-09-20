package efs.validation.meta;


import com.gmail.rgizmalkov.dev.meta.Validation;
import efs.validation.core.meta.RelatedValidator;
import efs.validation.meta.related.PatternValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Validation
@RelatedValidator(PatternValidator.class)
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Pattern {
    String value();
}
