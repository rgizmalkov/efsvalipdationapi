package com.gmail.rgizmalkov.dev.validation;

import com.gmail.rgizmalkov.dev.ival.ValidationResponse;

interface ValidationExecutor {

    ValidationResponse validate(RecursiveDecomposeObjectService.DecomposedObject decomposedObject);
}
