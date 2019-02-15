package com.github.universalreader;

import lombok.Getter;

@Getter
public class ReaderConfiguration {

    private static final String DEFAULT_RECORDS_SEPARATOR = ";";
    private static final String DEFAULT_FIELDS_SEPARATOR = ",";
    private static final int DEFAULT_START_INDEX = 0;

    private String recordsSeparator;
    private String fieldsSeparator;
    private int startLineIndex;

    private ReaderConfiguration() {
        this.recordsSeparator = DEFAULT_RECORDS_SEPARATOR;
        this.fieldsSeparator = DEFAULT_FIELDS_SEPARATOR;
        this.startLineIndex = DEFAULT_START_INDEX;
    }

    public static ReaderConfiguration defaultReaderConfiguration() {
        return new ReaderConfiguration();
    }
}
