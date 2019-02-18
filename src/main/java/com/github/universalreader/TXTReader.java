package com.github.universalreader;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import static com.github.universalreader.util.ReaderUtil.inputStreamToBufferedReader;
import static com.github.universalreader.util.ReaderUtil.readRecord;

@UtilityClass
public class TXTReader {

    public static <T, E> ReaderResult<T, E> readRecords(FileSource fileSource, ContentHandler<T, E> contentsHandler,
                                                        ReaderConfiguration configuration)
            throws IOException {
        try (InputStream inputStream = fileSource.getInputStream();
             BufferedReader reader = inputStreamToBufferedReader(inputStream)) {

            reader.lines().skip(configuration.getStartLineIndex())
                    .forEach(line -> readRecord(line, contentsHandler, configuration.getFieldsSeparator()));

            return new ReaderResult<>(contentsHandler.getRecords(), contentsHandler.getErrorRecords());
        }
    }
}
