package com.github.boggard.universalreader.util;

import lombok.experimental.UtilityClass;
import org.mozilla.universalchardet.UnicodeBOMInputStream;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class ReaderUtil {

    public static BufferedReader inputStreamToBufferedReader(InputStream inputStream)
            throws IOException {
        Charset defaultCharset = StandardCharsets.UTF_8;
        BufferedInputStream fis = new BufferedInputStream(inputStream);

        fis.mark(fis.available() + 1);
        String detectedEncoding = CharsetUtil.detectCharset(fis);
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
