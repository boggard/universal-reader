package com.github.universalreader;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class ReaderResult<T, E> {

    private final List<T> records;
    private final List<E> errorRecords;

    public ReaderResult(Collection<T> records, Collection<E> errorRecords) {
        this.records = new ArrayList<>(records);
        this.errorRecords = new ArrayList<>(errorRecords);
    }
}
