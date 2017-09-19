package com.gmail.rgizmalkov.dev.register;

import com.gmail.rgizmalkov.dev.facade.KVStorage;
import com.gmail.rgizmalkov.dev.ival.Status;

import java.lang.annotation.Annotation;

public class RegistrationService {

    KVStorage storage;
    Register register;

    public RegistrationService(KVStorage storage, Register register) {
        this.storage = storage;
        this.register = register;
    }

    public AnnotationInstruction instruction(Annotation acls){
        Registration registration = register.getRegistration(acls);
        return attempt(registration);
    }

    public AnnotationInstruction attempt(Registration registration){
        Status status = storage.get(registration.getCriticality(), Status.class);
        Boolean aBoolean = storage.get(registration.getDisable(), Boolean.class);
        return new SimpleAnnotationInstruction(status, aBoolean);
    }
}
