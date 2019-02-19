package com.github.universalreader;

import com.github.universalreader.contenthandler.ContentHandler;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import static com.github.universalreader.util.ReaderUtil.inputStreamToBufferedReader;
import static com.github.universalreader.util.ReaderUtil.readRecord;

@UtilityClass
public class TXTReader {

    public static <R> R readRecords(FileSource fileSource, ContentHandler<R> contentsHandler,
                                                  ReaderConfiguration configuration)
            throws IOException {
        try (InputStream inputStream = fileSource.getInputStream();
             BufferedReader reader = inputStreamToBufferedReader(inputStream)) {

            reader.lines().skip(configuration.getStartLineIndex())
                    .forEach(line -> readRecord(line, contentsHandler, configuration.getFieldsSeparator()));

            contentsHandler.endFile();

            return contentsHandler.getResult();
        }
    }
}
