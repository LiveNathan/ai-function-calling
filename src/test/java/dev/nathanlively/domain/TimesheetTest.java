package dev.nathanlively.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class TimesheetTest {
    @Test
    void clockIn() throws Exception {
        Timesheet timesheet = new Timesheet(null);
        assertThat(timesheet.timeSheetEntries()).isEmpty();

        timesheet.clockIn(Instant.now());

        assertThat(timesheet.mostRecentEntry().workPeriod().start()).isNotNull();
        assertThat(timesheet.mostRecentEntry().workPeriod().end()).isNull();
    }

    // clock in with a project
    @Test
    void clockInWithProject() throws Exception {
        Timesheet timesheet = new Timesheet(null);
        assertThat(timesheet.timeSheetEntries()).isEmpty();

        timesheet.clockInWithProject(new Project("Project A"), Instant.now());

        assertThat(timesheet.mostRecentEntry().workPeriod().start()).isNotNull();
        assertThat(timesheet.mostRecentEntry().workPeriod().end()).isNull();
        assertThat(timesheet.mostRecentEntry().project()).isNotNull();
    }

    // clock out

    // clock in given no clock out, clock out automatically
}