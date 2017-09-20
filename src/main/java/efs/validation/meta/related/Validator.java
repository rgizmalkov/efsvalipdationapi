package efs.validation.meta.related;

import com.gmail.rgizmalkov.dev.ival.ValidationResponse;

public interface Validator {
    ValidationResponse validation(Object o);
}
