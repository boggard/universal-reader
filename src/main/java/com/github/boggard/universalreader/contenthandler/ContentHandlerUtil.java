package com.github.boggard.universalreader.contenthandler;

import com.github.boggard.universalreader.ReaderOriginalField;
import lombok.experimental.UtilityClass;

import java.util.function.Consumer;
import java.util.function.Function;

@UtilityClass
public class ContentHandlerUtil {

    public static <T> void setCalculatedField(String originalValue, Function<String, T> mapper, Consumer<T> validSetter,
                                               Consumer<ReaderOriginalField> invalidSetter, boolean nullable) {
        try {
            if (originalValue != null) {
                T value = mapper.apply(originalValue);
                setField(originalValue, value, validSetter, invalidSetter, false);
            } else {
                setField(null, null, validSetter, invalidSetter, nullable);
            }
        } catch (Exception ex) {
            setField(originalValue, null, validSetter, invalidSetter, false);
        }
    }

    public static void setField(String originalValue, Consumer<String> validSetter,
                                 Consumer<ReaderOriginalField> invalidSetter, boolean nullable) {
        setField(originalValue, originalValue, validSetter, invalidSetter, nullable);
    }

    public static <T> void setField(String originalValue, T value, Consumer<T> validSetter,
                                     Consumer<ReaderOriginalField> invalidSetter, boolean nullable) {
        if (nullable || value != null) {
            validSetter.accept(value);
            invalidSetter.accept(new ReaderOriginalField(originalValue, true));
        } else {
            invalidSetter.accept(new ReaderOriginalField(originalValue, false));
        }
    }
}
