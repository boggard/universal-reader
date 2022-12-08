package com.github.boggard.universalreader.util;

import com.github.boggard.universalreader.ReaderConfiguration;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.util.stream.Stream;

@UtilityClass
public class StringStreamUtil {

    public static Stream<String> stringStream(BufferedReader reader, ReaderConfiguration configuration) {
        if (configuration.isParallelProcessing()) {
            return reader.lines().parallel();
        } else {
            return reader.lines();
        }
    }
}
