package com.github.boggard.universalreader.contenthandler;

public interface RecordHandler<R> {

    default R handleAndReturnResult(String record, String fieldsSeparator) {
        R rec = startRecord();

        String[] fields = record.split(fieldsSeparator);
        for (int i = 0; i < fields.length; i++) {
            startField(rec, i, fields[i]);
        }

        return endRecord(rec);
    }

    R startRecord();

    void startField(R record, int index, String field);

    R endRecord(R record);
}
