package efs.validation.meta.related;

import com.gmail.rgizmalkov.dev.ival.Status;
import com.gmail.rgizmalkov.dev.ival.ValidationResponse;

public class PatternValidator implements Validator {
    @Override
    public ValidationResponse validation(Object o) {
        boolean isPassed = o == null;
        return new ValidationResponse("Null validation", isPassed ? "success" : "error", isPassed ? Status.OK : Status.FAILED);
    }
}
