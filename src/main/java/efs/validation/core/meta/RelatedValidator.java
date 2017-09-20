package efs.validation.core.meta;

import efs.validation.meta.related.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RelatedValidator {
    Class<? extends Validator> value();
}
