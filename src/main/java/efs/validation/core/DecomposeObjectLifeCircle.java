package efs.validation.core;

import com.gmail.rgizmalkov.dev.ival.ValidationResponse;

/**
 * Not Tread-Safe
 */
public interface DecomposeObjectLifeCircle<O> {

    DecomposeObjectLifeCircle init(O petal);
    DecomposeObjectLifeCircle validate();
    DecomposeObjectLifeCircle dataManagement();

    ValidationResponse response();
}
