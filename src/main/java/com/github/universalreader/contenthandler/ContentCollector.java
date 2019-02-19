package com.github.universalreader.contenthandler;

import com.github.universalreader.readerresult.CollectResult;

import java.util.Collection;

public interface ContentCollector<T, E> extends ContentHandler<E> {

    Collection<T> getRecords();

    @Override
    default CollectResult<T, E> getResult() {
        return new CollectResult<>(getRecords(), getErrorRecords());
    }
}
