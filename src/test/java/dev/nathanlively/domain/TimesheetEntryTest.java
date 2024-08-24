package dev.nathanlively.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimesheetEntryTest {

    private Project project;
    private TimesheetEntry entry;

    @BeforeEach
    void setUp() {
        project = new Project("Project A", new ArrayList<>());
        entry = new TimesheetEntry(Instant.now(), null, null, project);
    }

    @Test
    void clockOutAndSetDuration() throws Exception {
        Instant clockOutTime = Instant.now();

        TimesheetEntry actual = entry.clockOutAndSetDuration(clockOutTime);

        assertThat(actual.clockOut()).isNotNull();
        assertThat(actual.duration()).isNotNull();
    }

    @Test
    void clockOutAndSetDuration_givenClockOutProceedsClockIn_throws() throws Exception {
        Instant clockOutTime = entry.clockIn().minusSeconds(3600); // 1 hour before clockIn
        assertThatThrownBy(() -> entry.clockOutAndSetDuration(clockOutTime))
                .isInstanceOf(InvalidClockOutTimeException.class)
                .hasMessage("Clock-out time cannot be before clock-in time.");
    }
}