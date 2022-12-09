package com.github.boggard.universalreader.contenthandler;

import java.util.stream.Stream;

public interface ContentHandler<R, T> {

    RecordHandler<T> recordHandler();

    default void endFile() {
        //by default do nothing
    }

    R getResult(Stream<T> resultStream);
}
