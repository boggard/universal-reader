package com.github.boggard.universalreader;

import com.github.boggard.universalreader.contenthandler.ContentHandler;
import com.github.boggard.universalreader.util.ReaderUtil;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class CSVReader {

    public static <R> R readRecords(FileSource inputStreamSource, ContentHandler<R> contentsHandler,
                                                  ReaderConfiguration configuration)
            throws IOException {
        try (InputStream inputStream = inputStreamSource.getInputStream();
             BufferedReader reader = ReaderUtil.inputStreamToBufferedReader(inputStream)) {

            reader.lines().skip(configuration.getStartLineIndex()).forEach(line -> {
                String[] lineRecords = line.split(configuration.getRecordsSeparator());
                for (String lineRecord : lineRecords) {
                    ReaderUtil.readRecord(lineRecord, contentsHandler, configuration.getFieldsSeparator());
                }
            });

            contentsHandler.endFile();

            return contentsHandler.getResult();
        }
    }
}
