package efs.validation.meta;

import efs.validation.meta.related.NotNullValidator;
import efs.validation.core.meta.RelatedValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@RelatedValidator(NotNullValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonNull {
    String disable() default "validation.api.nonnull.disable";
}
