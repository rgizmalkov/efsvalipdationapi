package efs.validation.core.meta;

import com.gmail.rgizmalkov.dev.ival.Format;
import efs.validation.core.events.SetDefaultValue;
import efs.validation.core.meta.behaviour.EventRegistrar;
import efs.validation.core.meta.behaviour.ThrowEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@DynamicManagedValue(priority = Integer.MIN_VALUE, handler = SetDefaultValue.class)
@ThrowEvent(event = "SET_DEFAULT_VALUE")
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Default {
    String value();
    Format format() default Format.JSON;
}
