package com.github.boggard.universalreader;

import com.github.boggard.universalreader.contenthandler.ContentHandler;
import com.github.boggard.universalreader.contenthandler.RecordHandler;
import com.github.boggard.universalreader.util.ReaderUtil;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import static com.github.boggard.universalreader.util.StringStreamUtil.stringStream;

@UtilityClass
public class TXTReader {

    public static <R, T> R readRecords(FileSource fileSource, ContentHandler<R, T> contentsHandler,
                                    ReaderConfiguration configuration)
            throws IOException {
        try (InputStream inputStream = fileSource.getInputStream();
             BufferedReader reader = ReaderUtil.inputStreamToBufferedReader(inputStream)) {

            RecordHandler<T> recordHandler = contentsHandler.recordHandler();
            Stream<T> resultStream = stringStream(reader, configuration).skip(configuration.getStartLineIndex())
                    .map(line -> recordHandler.handleAndReturnResult(line, configuration.getFieldsSeparator()));

            contentsHandler.endFile();

            return contentsHandler.getResult(resultStream);
        }
    }
}
