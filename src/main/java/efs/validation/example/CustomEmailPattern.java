package efs.validation.example;

import com.gmail.rgizmalkov.dev.meta.Pattern;
import com.gmail.rgizmalkov.dev.meta.Validation;
import efs.validation.core.meta.Complex;
import efs.validation.core.meta.Composite;
import efs.validation.core.meta.behaviour.ExcludeOn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Complex
@ExcludeOn(key = "key.key.key")
@Pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomEmailPattern {
}
