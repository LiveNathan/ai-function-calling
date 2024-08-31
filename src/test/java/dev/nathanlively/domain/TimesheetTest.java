package dev.nathanlively.domain;

import dev.nathanlively.domain.exceptions.IncompleteEntryException;
import dev.nathanlively.domain.exceptions.NoTimesheetEntriesException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimesheetTest {
    @Test
    void clockIn() throws Exception {
        Timesheet timesheet = new Timesheet(null);
        assertThat(timesheet.timeSheetEntries()).isEmpty();

        timesheet.clockIn(Instant.now());

        assertThat(timesheet.mostRecentEntry().workPeriod().start()).isNotNull();
        assertThat(timesheet.mostRecentEntry().workPeriod().end()).isNull();
    }

    @Test
    void clockInWithProject() throws Exception {
        Timesheet timesheet = new Timesheet(null);
        assertThat(timesheet.timeSheetEntries()).isEmpty();

        timesheet.clockInWithProject(new Project("Project A"), Instant.now());

        assertThat(timesheet.mostRecentEntry().project()).isNotNull();
    }

    @Test
    void mostRecentEntryThrowsNoTimesheetEntriesException() {
        Timesheet timesheet = new Timesheet(new ArrayList<>());

        assertThatThrownBy(timesheet::mostRecentEntry)
                .isInstanceOf(NoTimesheetEntriesException.class)
                .hasMessage("No timesheet entries found.");
    }

    @Test
    void clockInThrowsIncompleteEntryException() {
        Timesheet timesheet = new Timesheet(null);
        timesheet.clockIn(Instant.now());

        assertThatThrownBy(() -> timesheet.clockIn(Instant.now()))
                .isInstanceOf(IncompleteEntryException.class)
                .hasMessage("Cannot clock in. The previous entry has not been clocked out.");
    }

    // clock out
    @Test
    void clockOut() throws Exception {
        Timesheet timesheet = new Timesheet(null);
        assertThat(timesheet.timeSheetEntries()).isEmpty();
        timesheet.clockIn(Instant.now());

        timesheet.clockOut(Instant.now().plusSeconds(60 * 2));

        assertThat(timesheet.mostRecentEntry().workPeriod().end()).isNotNull();
    }

    // clock in given no clock out, clock out automatically
}