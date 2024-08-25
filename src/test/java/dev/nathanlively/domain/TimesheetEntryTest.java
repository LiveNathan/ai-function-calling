package dev.nathanlively.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class TimesheetEntryTest {

    private Project project;
    private TimesheetEntry entry;

    @BeforeEach
    void setUp() {
        project = new Project("Project A", new ArrayList<>());
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

}