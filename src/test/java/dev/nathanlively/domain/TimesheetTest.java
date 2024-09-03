package dev.nathanlively.domain;

import dev.nathanlively.domain.exceptions.AlreadyClockedOutException;
import dev.nathanlively.domain.exceptions.NoTimesheetEntriesException;
import dev.nathanlively.domain.exceptions.OverlappingWorkPeriodException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimesheetTest {
    @Test
    void clockIn() {
        Timesheet timesheet = Timesheet.withSystemClock(null);
        assertThat(timesheet.timeSheetEntries()).isEmpty();

        timesheet.clockIn(Instant.now());

        assertThat(timesheet.mostRecentEntry().workPeriod().start()).isNotNull();
        assertThat(timesheet.mostRecentEntry().workPeriod().end()).isNull();
    }

    @Test
    void clockInWithProject() {
        Timesheet timesheet = Timesheet.withSystemClock(null);
        assertThat(timesheet.timeSheetEntries()).isEmpty();

        timesheet.clockInWithProject(new Project("Project A"), Instant.now());

        assertThat(timesheet.mostRecentEntry().project()).isNotNull();
    }

    @Test
    void mostRecentEntryThrowsNoTimesheetEntriesException() {
        Timesheet timesheet = Timesheet.withSystemClock(new ArrayList<>());

        assertThatThrownBy(timesheet::mostRecentEntry)
                .isInstanceOf(NoTimesheetEntriesException.class)
                .hasMessage("No timesheet entries found.");
    }

    @Test
    void clockOut() {
        Timesheet timesheet = Timesheet.withSystemClock(null);
        assertThat(timesheet.timeSheetEntries()).isEmpty();
        timesheet.clockIn(Instant.now().minusSeconds(60*2));

        timesheet.clockOut(Instant.now());

        assertThat(timesheet.mostRecentEntry().workPeriod().end()).isNotNull();
    }

    @Test
    void clockOutThrowsAlreadyClockedOutException() {
        Timesheet timesheet = Timesheet.withSystemClock(null);
        timesheet.clockIn(Instant.now().minusSeconds(60*2));
        timesheet.clockOut(Instant.now());

        assertThatThrownBy(() -> timesheet.clockOut(Instant.now().plusSeconds(60 * 3)))
                .isInstanceOf(AlreadyClockedOutException.class)
                .hasMessage("Cannot clock out. The most recent entry is already clocked out.");
    }

    @Test
    void clockIn_givenNullClockOut_clockOutAutomaticallyThenIn() {
        Timesheet timesheet = Timesheet.withSystemClock(null);
        timesheet.clockIn(Instant.now().minusSeconds(60*2));

        timesheet.clockIn(Instant.now());

        assertThat(timesheet.timeSheetEntries().size()).isEqualTo(2);
        assertThat(timesheet.mostRecentEntry().workPeriod().end()).isNull();
    }

    @Test
    void appendEntry_givenOverlap_throwsException() {
        Timesheet timesheet = Timesheet.withSystemClock(null);
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
        Timesheet timesheet = Timesheet.withSystemClock(null);
        Project project = new Project("Project A");
        Duration duration = Duration.ofHours(1);
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 8, 0);
        LocalDateTime end = LocalDateTime.of(2024, 3, 15, 9, 0);
        ZoneId zone = ZoneId.of("America/Chicago");
        Instant expectedStartInstant = start.atZone(zone).toInstant();
        Instant expectedEndInstant = end.atZone(zone).toInstant();
        TimesheetEntry expected = TimesheetEntry.clockIn(project, expectedStartInstant);
        expected.clockOut(expectedEndInstant);

        timesheet.appendEntryWithDuration(project, duration, zone);

        assertThat(timesheet.timeSheetEntries().getLast())
                .isEqualTo(expected);
    }

    @Test
    void calculateNextAvailableSlot_givenEmptyTimesheet_startAt9am() throws Exception {
        ZoneId zone = ZoneId.of("America/Chicago");
        Instant fixedInstant = LocalDate.of(2024, 9, 3).atStartOfDay(zone).toInstant();
        Timesheet timesheet = Timesheet.withFixedClock(null, fixedInstant);
        LocalDateTime todayAt9am = LocalDateTime.of(2024, 9, 3, 9, 0);
        Instant expected = todayAt9am.atZone(zone).toInstant();

        Instant actual = timesheet.calculateNextAvailableSlot(zone);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void calculateNextAvailableSlot_givenEmptyTimesheetAndDifferentDate_startAt9am() throws Exception {
        ZoneId zone = ZoneId.of("America/Chicago");
        Instant fixedInstant = LocalDate.of(2024, 9, 2).atStartOfDay(zone).toInstant();
        Timesheet timesheet = Timesheet.withFixedClock(null, fixedInstant);
        LocalDateTime today = LocalDateTime.of(2024, 9, 2, 9, 0);
        Instant expected = today.atZone(zone).toInstant();

        Instant actual = timesheet.calculateNextAvailableSlot(zone);

        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void calculateNextAvailableSlot_givenNonEmptyTimesheet_startAt10am() throws Exception {
        ZoneId zone = ZoneId.of("America/Chicago");
        Instant fixedInstant = LocalDate.of(2024, 9, 2).atStartOfDay(zone).toInstant();
        Timesheet timesheet = Timesheet.withFixedClock(null, fixedInstant);
        Project project = new Project("Project A");
        LocalDateTime start = LocalDateTime.of(2024, 9, 2, 9, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 2, 10, 0);
        TimesheetEntry timesheetEntry = TimesheetEntry.from(project, start, end, zone);
        timesheet.appendEntry(timesheetEntry);
        LocalDateTime today = LocalDateTime.of(2024, 9, 2, 10, 0);
        Instant expected = today.atZone(zone).toInstant();

        Instant actual = timesheet.calculateNextAvailableSlot(zone);

        assertThat(actual)
                .isEqualTo(expected);
    }
}