package dev.nathanlively.domain;

import dev.nathanlively.domain.exceptions.AlreadyClockedOutException;
import dev.nathanlively.domain.exceptions.NoTimesheetEntriesException;
import dev.nathanlively.domain.exceptions.OverlappingWorkPeriodException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.*;
import java.util.ArrayList;
import java.util.stream.Stream;

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

    private static final ZoneId ZONE_ID = ZoneId.of("America/Chicago");

    // Method to provide arguments for the parameterized test
    private static Stream<Arguments> timesheetParameters() {
        return Stream.of(
                // Arguments: fixedInstant, startDateTime, entries, expected
                Arguments.of(
                        LocalDate.of(2024, 9, 3).atStartOfDay(ZONE_ID).toInstant(),
                        LocalDateTime.of(2024, 9, 3, 9, 0),
                        new TimesheetEntry[]{},
                        LocalDateTime.of(2024, 9, 3, 9, 0).atZone(ZONE_ID).toInstant()
                ),
                Arguments.of(
                        LocalDate.of(2024, 9, 2).atStartOfDay(ZONE_ID).toInstant(),
                        LocalDateTime.of(2024, 9, 2, 9, 0),
                        new TimesheetEntry[]{},
                        LocalDateTime.of(2024, 9, 2, 9, 0).atZone(ZONE_ID).toInstant()
                ),
                Arguments.of(
                        LocalDate.of(2024, 9, 2).atStartOfDay(ZONE_ID).toInstant(),
                        LocalDateTime.of(2024, 9, 2, 10, 0),
                        new TimesheetEntry[]{TimesheetEntry.from(new Project("Project A"), LocalDateTime.of(2024, 9, 2, 9, 0), LocalDateTime.of(2024, 9, 2, 10, 0), ZONE_ID)},
                        LocalDateTime.of(2024, 9, 2, 10, 0).atZone(ZONE_ID).toInstant()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("timesheetParameters")
    void calculateNextAvailableSlot(Instant fixedInstant, LocalDateTime startDateTime, TimesheetEntry[] entries, Instant expected) {
        Timesheet timesheet = Timesheet.withFixedClock(null, fixedInstant);
        for (TimesheetEntry entry : entries) {
            timesheet.appendEntry(entry);
        }
        Instant actual = timesheet.calculateNextAvailableSlot(ZONE_ID);
        assertThat(actual).isEqualTo(expected);
    }
}