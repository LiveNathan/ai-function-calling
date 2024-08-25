package dev.nathanlively.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkPeriodTest {
    @Test
    void newWorkPeriod_givenEndProceedsStart_throws() throws Exception {
        assertThatThrownBy(() -> new WorkPeriod(Instant.now(), Instant.now().minusSeconds(3600)))
                .isInstanceOf(InvalidClockOutTimeException.class)
                .hasMessage("End time cannot be before start time.");
    }

    @Test
    void setEnd_givenEndProceedsStart_throws() throws Exception {
        WorkPeriod workPeriod = WorkPeriod.startAt(Instant.now());
        assertThatThrownBy(() -> workPeriod.setEnd(Instant.now().minusSeconds(3600)))
                .isInstanceOf(InvalidClockOutTimeException.class)
                .hasMessage("End time cannot be before start time.");
    }

}