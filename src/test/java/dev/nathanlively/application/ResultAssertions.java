package dev.nathanlively.application;

import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;

public class ResultAssertions extends Assertions {
    @NotNull
    public static <T> ResultAssert<T> assertThat(Result<T> result) {
        return new ResultAssert<>(result);
    }
}
