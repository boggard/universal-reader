package com.github.universalreader.contenthandler;

import com.github.universalreader.readerresult.ReaderResult;

import java.util.Collection;

public interface ContentHandler<E> {

    void startRecord();

    void startField(int index, String field);

    void endRecord();

    Collection<E> getErrorRecords();

    ReaderResult<E> getResult();
}
