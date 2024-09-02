package dev.nathanlively.domain;

import dev.nathanlively.domain.exceptions.InvalidClockOutTimeException;
import dev.nathanlively.domain.exceptions.InvalidWorkPeriodException;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkPeriodTest {
    @Test
    void newWorkPeriod_givenEndProceedsStart_throws() {
        assertThatThrownBy(() -> new WorkPeriod(Instant.now(), Instant.now().minusSeconds(3600)))
                .isInstanceOf(InvalidClockOutTimeException.class)
                .hasMessage("End time cannot be before start time.");
    }

    @Test
    void setEnd_givenEndProceedsStart_throws() {
        WorkPeriod workPeriod = WorkPeriod.startAt(Instant.now());
        assertThatThrownBy(() -> workPeriod.setEnd(Instant.now().minusSeconds(3600)))
                .isInstanceOf(InvalidClockOutTimeException.class)
                .hasMessage("End time cannot be before start time.");
    }

    @Test
    void newWorkPeriod_givenStartTimeInFuture_throws() {
        assertThatThrownBy(() -> new WorkPeriod(Instant.now().plusSeconds(3600), Instant.now().plusSeconds(7200)))
                .isInstanceOf(InvalidWorkPeriodException.class)
                .hasMessage("Start time cannot be in the future.");
    }

    @Test
    void newWorkPeriod_givenEndTimeInFuture_throws() {
        assertThatThrownBy(() -> new WorkPeriod(Instant.now(), Instant.now().plusSeconds(3600)))
                .isInstanceOf(InvalidWorkPeriodException.class)
                .hasMessage("End time cannot be in the future.");
    }

    @Test
    void setEnd_givenEndTimeInFuture_throws() {
        WorkPeriod workPeriod = WorkPeriod.startAt(Instant.now());
        assertThatThrownBy(() -> workPeriod.setEnd(Instant.now().plusSeconds(3600)))
                .isInstanceOf(InvalidWorkPeriodException.class)
                .hasMessage("End time cannot be in the future.");
    }

    @Test
    void overlaps_givenNoOverlap_returnsFalse() throws Exception {
        WorkPeriod workPeriod1 = new WorkPeriod(
                Instant.now().minusSeconds(60 * 60),
                Instant.now().minusSeconds(60 * 30));
        WorkPeriod workPeriod2 = new WorkPeriod(
                Instant.now().minusSeconds(60 * 29),
                Instant.now());

        boolean actual = workPeriod1.overlaps(workPeriod2);

        assertThat(actual).isFalse();
    }
}