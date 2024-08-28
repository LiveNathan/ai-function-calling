package dev.nathanlively.application;
import dev.nathanlively.domain.TimesheetEntry;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;

public class ResultAssertions extends Assertions {
    @NotNull
    public static ResultAssert assertThat(Result<TimesheetEntry> result) {
        return new ResultAssert(result);
    }
}
