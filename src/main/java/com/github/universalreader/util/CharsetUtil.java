package com.github.universalreader.util;

import lombok.experimental.UtilityClass;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedInputStream;
import java.io.IOException;

@UtilityClass
public class CharsetUtil {

    /**
     * Метод для определения кодировки исходного {@link java.io.BufferedInputStream}
     * @param fis исходный {@link BufferedInputStream}
     * @return определенная кодировка или {@code null}, если определить не удалось
     * @throws IOException в случае ошибок в процессе работы с потоками
     */
    public static String detectCharset(BufferedInputStream fis) throws IOException {
        byte[] buf = new byte[4096];

        UniversalDetector detector = new UniversalDetector();

        int nRead;
        while ((nRead = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nRead);
        }
        detector.dataEnd();

        String encoding = detector.getDetectedCharset();
        detector.reset();

        return encoding;
    }
}
