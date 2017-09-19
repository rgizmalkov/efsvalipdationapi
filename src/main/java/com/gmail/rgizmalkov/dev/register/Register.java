package com.gmail.rgizmalkov.dev.register;

import com.gmail.rgizmalkov.dev.ival.Status;
import com.gmail.rgizmalkov.dev.meta.NonNull;
import com.gmail.rgizmalkov.dev.meta.ValidationQualifier;
import com.gmail.rgizmalkov.dev.vdf.DefaultValues;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class Register {

    Map<Class<? extends Annotation>, Registrator> register;
    String defaultStatus = DefaultValues.DEFAULT_CRITICALITY.getKey();
    String defaultDisabledSign = DefaultValues.DEFAULT_DISABLE.getKey();

    public Register(Map<Class<? extends Annotation>, Registrator> register) {
        this.register = register;
    }

    public Register() {
        this.register = new HashMap<>();
        this.register.put(NonNull.class, new NonNullRegistrator());
        this.register.put(ValidationQualifier.class, new ValidationQualifierRegistrator());
    }

    public <A extends  Annotation> Register setRegister(Class<A> clsreg, Registrator<A> register) {
        this.register.put(clsreg, register);
        return this;
    }

    @SuppressWarnings("unchecked")
    public<A extends Annotation> Registration getRegistration(/*NonNull*/A annon){
        if(annon == null) return null;

        Registrator registrator = register.get(annon.annotationType());
        return registrator != null ? registrator.register(annon) : new Registration(defaultStatus, defaultDisabledSign);
    }
}
