package com.github.universalreader;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import static com.github.universalreader.util.ReaderUtil.inputStreamToBufferedReader;
import static com.github.universalreader.util.ReaderUtil.readRecord;

@UtilityClass
public class CSVReader {

    public static <T, E> ReaderResult<T, E> readRecords(FileSource inputStreamSource, ContentHandler<T, E> contentsHandler,
                                                  ReaderConfiguration configuration)
            throws IOException {
        try (InputStream inputStream = inputStreamSource.getInputStream();
             BufferedReader reader = inputStreamToBufferedReader(inputStream)) {
            List<T> records = new LinkedList<>();
            List<E> errorRecords = new LinkedList<>();

            reader.lines().skip(configuration.getStartLineIndex()).forEach(line -> {
                String[] lineRecords = line.split(configuration.getRecordsSeparator());
                for (String lineRecord : lineRecords) {
                    readRecord(lineRecord, contentsHandler, configuration.getFieldsSeparator(),
                            records::add, errorRecords::add);
                }
            });

            return new ReaderResult<>(records, errorRecords);
        }
    }
}
