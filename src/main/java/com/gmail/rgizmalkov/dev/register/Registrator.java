package com.gmail.rgizmalkov.dev.register;

import java.lang.annotation.Annotation;

public interface Registrator<A extends Annotation> {

    Registration register(A annotation);
}
