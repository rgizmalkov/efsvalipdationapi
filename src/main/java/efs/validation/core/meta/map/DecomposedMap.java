package efs.validation.core.meta.map;

import efs.validation.core.meta.stratgy.ObjectStrategy;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DecomposedMap {
    Class<?> target();
    DecomposedElement[] elements() default {};
}
