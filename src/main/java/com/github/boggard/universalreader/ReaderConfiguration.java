package com.github.boggard.universalreader;

import lombok.Getter;

@Getter
public class ReaderConfiguration {

    private static final String DEFAULT_RECORDS_SEPARATOR = ";";
    private static final String DEFAULT_FIELDS_SEPARATOR = ",";
    private static final int DEFAULT_START_INDEX = 0;
    private static final boolean DEFAULT_PARALLEL_PROCESSING = true;

    private String recordsSeparator;
    private String fieldsSeparator;
    private int startLineIndex;
    private boolean parallelProcessing;

    private ReaderConfiguration() {
        this(DEFAULT_START_INDEX);
    }

    private ReaderConfiguration(int startLineIndex) {
        this(DEFAULT_RECORDS_SEPARATOR, DEFAULT_FIELDS_SEPARATOR, startLineIndex, DEFAULT_PARALLEL_PROCESSING);
    }

    private ReaderConfiguration(String recordsSeparator, String fieldsSeparator, int startLineIndex,
                                boolean parallelProcessing) {
        this.recordsSeparator = recordsSeparator;
        this.fieldsSeparator = fieldsSeparator;
        this.startLineIndex = startLineIndex;
        this.parallelProcessing = parallelProcessing;
    }

    public static ReaderConfiguration defaultReaderConfiguration() {
        return new ReaderConfiguration();
    }

    public static ReaderConfiguration withHeaderDefaultConfiguration() {
        return new ReaderConfiguration(1);
    }

    public static ReaderConfiguration customConfiguration(String recordsSeparator, String fieldsSeparator, int startLineIndex,
                                                          boolean parallelProcessing) {
        return new ReaderConfiguration(recordsSeparator, fieldsSeparator, startLineIndex, parallelProcessing);
    }
}
