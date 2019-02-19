package com.github.universalreader.contenthandler;

public interface ContentHandler<R> {

    void startRecord();

    void startField(int index, String field);

    void endRecord();

    default void endFile() {
        //by default do nothing
    }

    R getResult();
}
