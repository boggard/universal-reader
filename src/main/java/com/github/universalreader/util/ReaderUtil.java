package com.github.universalreader.util;

import com.github.universalreader.ContentHandler;
import lombok.experimental.UtilityClass;
import org.mozilla.universalchardet.UnicodeBOMInputStream;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Consumer;

import static com.github.universalreader.util.CharsetUtil.detectCharset;

@UtilityClass
public class ReaderUtil {

    public static <T, E> void readRecord(String record, ContentHandler<T, E> contentsHandler, String recordsSeparator,
                                         Consumer<T> onSuccess, Consumer<E> onError) {
        contentsHandler.startRecord();

        String[] fields = record.split(recordsSeparator);
        for (int i = 0; i < fields.length; i++) {
            contentsHandler.startField(i, fields[i]);
        }

        readRecord(contentsHandler, onSuccess, onError);
    }

    public static <T, E> void readRecord(ContentHandler<T, E> contentsHandler, Consumer<T> onSuccess, Consumer<E> onError) {
        Optional<T> record = contentsHandler.endRecord();

        if (record.isPresent()) {
            onSuccess.accept(record.get());
        } else {
            contentsHandler.getErrorRecord().ifPresent(onError);
        }
    }

    public static BufferedReader inputStreamToBufferedReader(InputStream inputStream)
            throws IOException {
        Charset defaultCharset = StandardCharsets.UTF_8;
        BufferedInputStream fis = new BufferedInputStream(inputStream);

        fis.mark(fis.available() + 1);
        String detectedEncoding = detectCharset(fis);
        fis.reset();

        if (detectedEncoding != null) {
            defaultCharset = Charset.forName(detectedEncoding);
        }
        if (!defaultCharset.toString().contains("UTF")) {
            return new BufferedReader(new InputStreamReader(fis, defaultCharset));
        }
        return new BufferedReader(new InputStreamReader(new UnicodeBOMInputStream(fis),
                defaultCharset));
    }
}
