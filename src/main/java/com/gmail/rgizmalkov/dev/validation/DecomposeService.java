package com.gmail.rgizmalkov.dev.validation;

import java.lang.reflect.Field;

public interface DecomposeService {
    RecursiveDecomposeObjectService.DecomposedObject decompose(Field field);
}
