package com.github.boggard.universalreader;

import lombok.Getter;

public class ReaderOriginalField {

    public static ReaderOriginalField DEFAULT_NULLABLE = new ReaderOriginalField(null, true);
    public static ReaderOriginalField DEFAULT_NONNULL = new ReaderOriginalField(null, false);

    @Getter
    private String value;
    private boolean valid;

    public ReaderOriginalField(String value, boolean valid) {
        this.value = value;
        this.valid = valid;
    }

    public boolean isInvalid() {
        return !valid;
    }

    @Override
    public String toString() {
        return value;
    }
}
