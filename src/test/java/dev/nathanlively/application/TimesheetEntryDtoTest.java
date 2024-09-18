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
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 8, 0);
        LocalDateTime end = LocalDateTime.of(2024, 3, 15, 9, 0);
        ZoneId zoneId = ZoneId.of("America/Chicago");
        Instant startInstant = start.atZone(zoneId).toInstant();
        Instant endInstant = end.atZone(zoneId).toInstant();
        Project projectA = Project.create("Project A");
        TimesheetEntry timesheetEntry = TimesheetEntry.from(projectA, startInstant, endInstant);
        TimesheetEntryDto expected = new TimesheetEntryDto(projectA.name(),
                WorkPeriodDto.from(timesheetEntry.workPeriod(), zoneId));

        TimesheetEntryDto actual = TimesheetEntryDto.from(timesheetEntry, zoneId);

        assertThat(actual)
                .isEqualTo(expected);
    }
}