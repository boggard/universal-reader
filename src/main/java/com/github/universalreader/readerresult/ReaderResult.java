package com.github.universalreader.readerresult;

import java.util.List;

public interface ReaderResult<E> {

    List<E> getErrorRecords();
}
