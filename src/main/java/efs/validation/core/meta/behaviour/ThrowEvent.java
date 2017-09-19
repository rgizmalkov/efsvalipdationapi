package efs.validation.core.meta.behaviour;

import efs.validation.core.events.EventHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ThrowEvent {
    String event();
//    Class<? extends EventHandler> handler();
}
