package efs.validation.core.meta.behaviour;

import com.gmail.rgizmalkov.dev.ival.Status;
import efs.validation.core.events.ReduceLevelEventHandler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@EventRegistrar(registration = "SET_DEFAULT_VALUE", handler = ReduceLevelEventHandler.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReduceLevel {
    Status from() default Status.FAILED;
    Status to() default Status.WARN;
}
