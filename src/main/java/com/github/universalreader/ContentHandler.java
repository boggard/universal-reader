package com.github.universalreader;

import java.util.Collection;

public interface ContentHandler<T, E> {

    void startRecord();

    void startField(int index, String field);

    void endRecord();

    Collection<T> getRecords();

    Collection<E> getErrorRecords();
}
