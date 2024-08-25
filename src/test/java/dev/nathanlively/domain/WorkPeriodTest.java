package dev.nathanlively.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkPeriodTest {
    @Test
    void clockOutAndSetDuration_givenClockOutProceedsClockIn_throws() throws Exception {
        assertThatThrownBy(() -> new WorkPeriod(Instant.now(), Instant.now().minusSeconds(3600)))
                .isInstanceOf(InvalidClockOutTimeException.class)
                .hasMessage("End time cannot be before start time.");
    }


}