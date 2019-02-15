package com.github.universalreader;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ReaderResult<T, E> {

    private final List<T> records;
    private final List<E> errorRecords;
}
