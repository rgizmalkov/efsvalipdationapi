package com.gmail.rgizmalkov.dev.vdf;

import com.gmail.rgizmalkov.dev.ival.Status;

public enum DefaultValues {
    DEFAULT_CRITICALITY("validation.api.criticality.default", Status.FAILED),
    DEFAULT_DISABLE("validation.api.disable.default", false);

    private String key;
    private Object value;

    DefaultValues(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
