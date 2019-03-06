package com.github.boggard.universalreader;

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
        this(DEFAULT_START_INDEX);
    }

    private ReaderConfiguration(int startLineIndex) {
        this(DEFAULT_RECORDS_SEPARATOR, DEFAULT_FIELDS_SEPARATOR, startLineIndex);
    }

    private ReaderConfiguration(String recordsSeparator, String fieldsSeparator, int startLineIndex) {
        this.recordsSeparator = recordsSeparator;
        this.fieldsSeparator = fieldsSeparator;
        this.startLineIndex = startLineIndex;
    }

    public static ReaderConfiguration defaultReaderConfiguration() {
        return new ReaderConfiguration();
    }

    public static ReaderConfiguration withHeaderDefaultConfiguration() {
        return new ReaderConfiguration(1);
    }

    public static ReaderConfiguration customConfiguration(String recordsSeparator, String fieldsSeparator, int startLineIndex) {
        return new ReaderConfiguration(recordsSeparator, fieldsSeparator, startLineIndex);
    }
}
