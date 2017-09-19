package efs.validation.core.meta.stratgy;

import efs.validation.enums.ValidationStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Strategy {
    ValidationStrategy value() default ValidationStrategy.ONE_FAIL_ALL_DOWN;
}
