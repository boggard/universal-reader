package com.github.boggard.universalreader;

import com.github.boggard.universalreader.contenthandler.ContentHandler;
import com.github.boggard.universalreader.util.ReaderUtil;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class TXTReader {

    public static <R> R readRecords(FileSource fileSource, ContentHandler<R> contentsHandler,
                                                  ReaderConfiguration configuration)
            throws IOException {
        try (InputStream inputStream = fileSource.getInputStream();
             BufferedReader reader = ReaderUtil.inputStreamToBufferedReader(inputStream)) {

            reader.lines().skip(configuration.getStartLineIndex())
                    .forEach(line -> ReaderUtil.readRecord(line, contentsHandler, configuration.getFieldsSeparator()));

            contentsHandler.endFile();

            return contentsHandler.getResult();
        }
    }
}
