package dev.nathanlively.domain;

import dev.nathanlively.domain.exceptions.AlreadyClockedOutException;
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
    void clockOut() throws Exception {
        Timesheet timesheet = new Timesheet(null);
        assertThat(timesheet.timeSheetEntries()).isEmpty();
        timesheet.clockIn(Instant.now());

        timesheet.clockOut(Instant.now().plusSeconds(60 * 2));

        assertThat(timesheet.mostRecentEntry().workPeriod().end()).isNotNull();
    }

    @Test
    void clockOutThrowsAlreadyClockedOutException() throws Exception {
        Timesheet timesheet = new Timesheet(null);
        timesheet.clockIn(Instant.now());
        timesheet.clockOut(Instant.now().plusSeconds(60 * 2));

        assertThatThrownBy(() -> timesheet.clockOut(Instant.now().plusSeconds(60 * 3)))
                .isInstanceOf(AlreadyClockedOutException.class)
                .hasMessage("Cannot clock out. The most recent entry is already clocked out.");
    }

    // clock in given no clock out, clock out automatically
    @Test
    void clockIn_givenNullClockOut_clockOutAutomaticallyThenIn() throws Exception {
        Timesheet timesheet = new Timesheet(null);
        timesheet.clockIn(Instant.now());

        timesheet.clockIn(Instant.now().plusSeconds(60 * 2));

        assertThat(timesheet.timeSheetEntries().size()).isEqualTo(2);
        assertThat(timesheet.mostRecentEntry().workPeriod().end()).isNull();
    }
}