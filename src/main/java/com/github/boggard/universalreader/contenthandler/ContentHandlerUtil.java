package com.github.boggard.universalreader.contenthandler;

import com.github.boggard.universalreader.ReaderOriginalField;
import lombok.experimental.UtilityClass;

import java.util.function.Consumer;
import java.util.function.Function;

@UtilityClass
public class ContentHandlerUtil {

    /**
     * Use {@link this#setCalculatedField(String, Function, Consumer, Consumer)}
     * or {@link this#setCalculatedField(String, Function, Consumer, Consumer, Function)}
     * instead
     */
    @Deprecated
    public static <T> void setCalculatedField(String originalValue, Function<String, T> mapper, Consumer<T> validSetter,
                                              Consumer<ReaderOriginalField> invalidSetter, boolean nullable) {
        setCalculatedField(originalValue, mapper, validSetter, invalidSetter, val -> nullable || val != null);
    }

    public static <T> void setCalculatedField(String originalValue, Function<String, T> mapper, Consumer<T> validSetter,
                                              Consumer<ReaderOriginalField> invalidSetter) {
        setCalculatedField(originalValue, mapper, validSetter, invalidSetter, val -> true);
    }

    public static <T> void setCalculatedField(String originalValue, Function<String, T> mapper, Consumer<T> validSetter,
                                              Consumer<ReaderOriginalField> invalidSetter, Function<T, Boolean> validator) {
        try {
            if (originalValue != null) {
                T value = mapper.apply(originalValue);
                setField(originalValue, value, validSetter, invalidSetter, validator);
            } else {
                setField(null, null, validSetter, invalidSetter, validator);
            }
        } catch (Exception ex) {
            setField(originalValue, null, validSetter, invalidSetter, validator);
        }
    }

    /**
     * Use {@link this#setField(String, Consumer, Consumer)}
     * or {@link this#setField(String, Consumer, Consumer, Function)}
     * instead
     */
    @Deprecated
    public static void setField(String originalValue, Consumer<String> validSetter,
                                Consumer<ReaderOriginalField> invalidSetter, boolean nullable) {
        setField(originalValue, validSetter, invalidSetter, val -> nullable || val != null);
    }

    public static void setField(String originalValue, Consumer<String> validSetter,
                                Consumer<ReaderOriginalField> invalidSetter) {
        setField(originalValue, originalValue, validSetter, invalidSetter, val -> true);
    }

    public static void setField(String originalValue, Consumer<String> validSetter,
                                Consumer<ReaderOriginalField> invalidSetter, Function<String, Boolean> validator) {
        setField(originalValue, originalValue, validSetter, invalidSetter, validator);
    }

    /**
     * Use {@link this#setField(String, Object, Consumer, Consumer)}
     * or {@link this#setField(String, Object, Consumer, Consumer, Function)}
     * instead
     */
    @Deprecated
    public static <T> void setField(String originalValue, T value, Consumer<T> validSetter,
                                    Consumer<ReaderOriginalField> invalidSetter, boolean nullable) {
        setField(originalValue, value, validSetter, invalidSetter, val -> nullable || val != null);
    }

    public static <T> void setField(String originalValue, T value, Consumer<T> validSetter,
                                    Consumer<ReaderOriginalField> invalidSetter) {
        setField(originalValue, value, validSetter, invalidSetter, val -> true);
    }

    public static <T> void setField(String originalValue, T value, Consumer<T> validSetter,
                                    Consumer<ReaderOriginalField> invalidSetter, Function<T, Boolean> validator) {
        if (validator.apply(value)) {
            validSetter.accept(value);
            invalidSetter.accept(new ReaderOriginalField(originalValue, true));
        } else {
            invalidSetter.accept(new ReaderOriginalField(originalValue, false));
        }
    }
}
