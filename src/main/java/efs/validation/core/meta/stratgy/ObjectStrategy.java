package efs.validation.core.meta.stratgy;

import efs.validation.core.meta.behaviour.ReduceLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Strategy
@ReduceLevel
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectStrategy {
}
