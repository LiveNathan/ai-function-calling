package dev.nathanlively.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class TimesheetEntryTest {

    private Project project;
    private TimesheetEntry entry;

    @BeforeEach
    void setUp() {
        project = new Project("Project A");
        entry =  TimesheetEntry.clockIn(project, Instant.now());
    }

    @Test
    void clockIn_createsNewEntry() throws Exception {
        TimesheetEntry actual = TimesheetEntry.clockIn(Instant.now());

        assertThat(actual.workPeriod()).isNotNull();
    }

    @Test
    void clockOutAndSetDuration() throws Exception {
        Instant clockOutTime = Instant.now();

        entry.clockOut(clockOutTime);

        assertThat(entry.workPeriod().end()).isNotNull();
        assertThat(entry.duration()).isNotNull();
    }

    @Test
    void from() throws Exception {
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 8, 0);
        LocalDateTime end = LocalDateTime.of(2024, 3, 15, 9, 0);
        ZoneId zone = ZoneId.of("America/Chicago");
        Instant expectedStartInstant = start.atZone(zone).toInstant();
        Instant expectedEndInstant = end.atZone(zone).toInstant();
        TimesheetEntry expected = TimesheetEntry.clockIn(project, expectedStartInstant);
        expected.clockOut(expectedEndInstant);

        TimesheetEntry actual = TimesheetEntry.from(project, start, end, zone);

        assertThat(actual)
                .isEqualTo(expected);
    }

}