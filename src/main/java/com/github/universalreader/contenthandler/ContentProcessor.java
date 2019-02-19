package com.github.universalreader.contenthandler;

import com.github.universalreader.readerresult.ProcessResult;

public interface ContentProcessor<E> extends ContentHandler<E> {

    int getSuccessCount();

    @Override
    default ProcessResult<E> getResult() {
        return new ProcessResult<>(getSuccessCount(), getErrorRecords());
    }
}
