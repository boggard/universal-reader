package com.github.universalreader;

import java.util.Optional;

public interface ContentHandler<T, E> {

    void startRecord();

    void startField(int index, String field);

    Optional<T> endRecord();

    Optional<E> getErrorRecord();
}
