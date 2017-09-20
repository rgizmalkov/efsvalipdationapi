package efs.validation.meta.related;


import com.gmail.rgizmalkov.dev.ival.ValidationResponse;

public class NotEmptyValidator implements Validator {
    @Override
    public ValidationResponse validation(Object o) {
        if (o == null || !(o instanceof String)) {
            return new ValidationResponse();
        }
        else
            return new ValidationResponse();
    }
}
