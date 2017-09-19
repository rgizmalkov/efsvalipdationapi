package efs.validation.decompose;

import efs.validation.core.Decomposed;
import efs.validation.core.meta.Default;

public final class DecomposedRs {
    Decomposed decomposed;
    Default defaultValue;

    public DecomposedRs(Decomposed decomposed, Default defaultValue) {
        this.decomposed = decomposed;
        this.defaultValue = defaultValue;
    }

    public Decomposed getDecomposed() {
        return decomposed;
    }

    public DecomposedRs setDecomposed(Decomposed decomposed) {
        this.decomposed = decomposed;
        return this;
    }

    public Default getDefaultValue() {
        return defaultValue;
    }

    public DecomposedRs setDefaultValue(Default defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
}
