package com.github.universalreader.readerresult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProcessResult<E> implements ReaderResult<E> {

    private final int successCount;
    private final List<E> errorResult;

    public ProcessResult(int successCount, Collection<E> errorResult) {
        this.successCount = successCount;
        this.errorResult = new ArrayList<>(errorResult);
    }

    public int getSuccessCount() {
        return successCount;
    }

    @Override
    public List<E> getErrorRecords() {
        return errorResult;
    }
}
