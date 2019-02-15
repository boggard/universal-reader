package com.github.universalreader.util;

import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.util.Strings;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@UtilityClass
public class CharsetUtil {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;


    /**
     * Метод, если кодировка {@code charset} равняется {@code null}, возвращает кодировку
     * по-умолчанию {@link CharsetUtil#DEFAULT_CHARSET}
     *
     * @param charset кодировка
     * @return кодировка
     */
    public static String ifNotSetUseDefaultCharset(Charset charset) {
        if (Objects.isNull(charset)) {
            return DEFAULT_CHARSET.name();
        }
        return charset.name();
    }

    /**
     * Метод, если кодировка {@code charset} равняется {@code null} или пустой строке, возвращает кодировку
     * по-умолчанию {@link CharsetUtil#DEFAULT_CHARSET}
     *
     * @param charset кодировка
     * @return кодировка
     */
    public static String ifNotSetUseDefaultCharset(String charset) {
        if (Strings.isNotBlank(charset)) {
            return charset;
        }
        return DEFAULT_CHARSET.name();
    }

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
