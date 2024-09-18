package dev.nathanlively.application;

import dev.nathanlively.domain.Project;
import dev.nathanlively.domain.TimesheetEntry;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class TimesheetEntryDtoTest {
    @Test
    void fromDomain() {
        ZoneId zoneId = ZoneId.of("America/Chicago");
        Instant start = LocalDateTime.of(2024, 3, 15, 9, 0).atZone(ZoneId.of("America/Chicago")).toInstant();
        String projectA = "Project A";
        TimesheetEntry timesheetEntry = TimesheetEntry.clockIn(new Project(projectA, 0), start);
        TimesheetEntryDto expected = new TimesheetEntryDto(projectA,
                WorkPeriodDto.from(timesheetEntry.workPeriod(), zoneId));

        TimesheetEntryDto actual = TimesheetEntryDto.from(timesheetEntry, zoneId);

        assertThat(actual)
                .isEqualTo(expected);
    }
}