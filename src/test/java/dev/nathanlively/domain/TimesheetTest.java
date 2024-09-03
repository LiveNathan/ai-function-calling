package dev.nathanlively.domain;

import dev.nathanlively.domain.exceptions.AlreadyClockedOutException;
import dev.nathanlively.domain.exceptions.NoTimesheetEntriesException;
import dev.nathanlively.domain.exceptions.OverlappingWorkPeriodException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimesheetTest {
    @Test
    void clockIn() {
        Timesheet timesheet = new Timesheet(null);
        assertThat(timesheet.timeSheetEntries()).isEmpty();

        timesheet.clockIn(Instant.now());

        assertThat(timesheet.mostRecentEntry().workPeriod().start()).isNotNull();
        assertThat(timesheet.mostRecentEntry().workPeriod().end()).isNull();
    }

    @Test
    void clockInWithProject() {
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
    void clockOut() {
        Timesheet timesheet = new Timesheet(null);
        assertThat(timesheet.timeSheetEntries()).isEmpty();
        timesheet.clockIn(Instant.now().minusSeconds(60*2));

        timesheet.clockOut(Instant.now());

        assertThat(timesheet.mostRecentEntry().workPeriod().end()).isNotNull();
    }

    @Test
    void clockOutThrowsAlreadyClockedOutException() {
        Timesheet timesheet = new Timesheet(null);
        timesheet.clockIn(Instant.now().minusSeconds(60*2));
        timesheet.clockOut(Instant.now());

        assertThatThrownBy(() -> timesheet.clockOut(Instant.now().plusSeconds(60 * 3)))
                .isInstanceOf(AlreadyClockedOutException.class)
                .hasMessage("Cannot clock out. The most recent entry is already clocked out.");
    }

    @Test
    void clockIn_givenNullClockOut_clockOutAutomaticallyThenIn() {
        Timesheet timesheet = new Timesheet(null);
        timesheet.clockIn(Instant.now().minusSeconds(60*2));

        timesheet.clockIn(Instant.now());

        assertThat(timesheet.timeSheetEntries().size()).isEqualTo(2);
        assertThat(timesheet.mostRecentEntry().workPeriod().end()).isNull();
    }

    @Test
    void appendEntry_givenOverlap_throwsException() {
        Timesheet timesheet = new Timesheet(null);
        timesheet.appendEntry(TimesheetEntry.from(new Project("Project A"),
                LocalDateTime.of(2023, 1, 1, 9, 0), LocalDateTime.of(2023, 1, 1, 12, 0), ZoneId.systemDefault()));

        assertThatThrownBy(() -> timesheet.appendEntry(TimesheetEntry.from(new Project("Project B"),
                LocalDateTime.of(2023, 1, 1, 11, 0), LocalDateTime.of(2023, 1, 1, 14, 0), ZoneId.systemDefault())))
                .isInstanceOf(OverlappingWorkPeriodException.class)
                .hasMessage("Work periods cannot overlap.");
    }

    @Test
    @Disabled("until next available slot")
    void appendEntry_givenProjectAndDuration() throws Exception {
        Timesheet timesheet = new Timesheet(null);
        Project project = new Project("Project A");
        Duration duration = Duration.ofHours(1);
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 8, 0);
        LocalDateTime end = LocalDateTime.of(2024, 3, 15, 9, 0);
        ZoneId zone = ZoneId.of("America/Chicago");
        Instant expectedStartInstant = start.atZone(zone).toInstant();
        Instant expectedEndInstant = end.atZone(zone).toInstant();
        TimesheetEntry expected = TimesheetEntry.clockIn(project, expectedStartInstant);
        expected.clockOut(expectedEndInstant);

        timesheet.appendEntryWithDuration(project, duration);

        assertThat(timesheet.timeSheetEntries().getLast())
                .isEqualTo(expected);
    }

    @Test
    void calculateNextAvailableSlot_givenEmptyTimesheet_startAt9am() throws Exception {
        Timesheet timesheet = new Timesheet(null);
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 9, 0);
        ZoneId zone = ZoneId.of("America/Chicago");
        Instant expected = start.atZone(zone).toInstant();

        Instant actual = timesheet.calculateNextAvailableSlot();

        assertThat(actual)
                .isEqualTo(expected);
    }
}