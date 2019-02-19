package com.github.universalreader.readerresult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectResult<T, E> implements ReaderResult<E> {

    private final List<T> records;
    private final List<E> errorRecords;

    public CollectResult(Collection<T> records, Collection<E> errorRecords) {
        this.records = new ArrayList<>(records);
        this.errorRecords = new ArrayList<>(errorRecords);;
    }

    public List<T> getRecords() {
        return records;
    }

    @Override
    public List<E> getErrorRecords() {
        return errorRecords;
    }
}
