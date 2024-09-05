package dev.nathanlively.common.validation;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.function.Supplier;

public interface ValidationUtils {
    static <T extends CharSequence> T requireNonEmpty(
            final T charSequence,
            final Supplier<String> messageSupplier
    ) {
        Objects.requireNonNull(charSequence, messageSupplier);

        if (charSequence.isEmpty()) {
            throw new IllegalArgumentException(messageSupplier.get());
        }

        return charSequence;
    }

    static <T extends CharSequence> T requireNonBlank(
            final T charSequence,
            final Supplier<String> messageSupplier
    ) {
        requireNonEmpty(charSequence, messageSupplier);

        if (StringUtils.isBlank(charSequence)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }

        return charSequence;
    }
}
