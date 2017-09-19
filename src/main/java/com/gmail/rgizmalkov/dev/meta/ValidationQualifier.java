package com.gmail.rgizmalkov.dev.meta;

import java.lang.annotation.*;


@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidationQualifier {
    Class<? extends Annotation> annotation();
    String criticality() default  "";
    String disable() default "";
}
