package com.github.universalreader.util;

import com.github.universalreader.ContentHandler;
import lombok.experimental.UtilityClass;
import org.mozilla.universalchardet.UnicodeBOMInputStream;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static com.github.universalreader.util.CharsetUtil.detectCharset;

@UtilityClass
public class ReaderUtil {

    public static <T, E> void readRecord(String record, ContentHandler<T, E> contentsHandler, String recordsSeparator) {
        contentsHandler.startRecord();

        String[] fields = record.split(recordsSeparator);
        for (int i = 0; i < fields.length; i++) {
            contentsHandler.startField(i, fields[i]);
        }

        readRecord(contentsHandler);
    }

    public static <T, E> void readRecord(ContentHandler<T, E> contentsHandler) {
        contentsHandler.endRecord();
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
