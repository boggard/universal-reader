package com.github.universalreader.contenthandler;

public interface ContentHandler<R> {

    void startRecord();

    void startField(int index, String field);

    void endRecord();

    R getResult();
}
