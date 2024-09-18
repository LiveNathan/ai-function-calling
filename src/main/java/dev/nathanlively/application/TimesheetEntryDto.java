package dev.nathanlively.application;

import dev.nathanlively.domain.TimesheetEntry;

import java.time.ZoneId;

public record TimesheetEntryDto(String projectName, WorkPeriodDto workPeriodDto) {

    public static TimesheetEntryDto from(TimesheetEntry timesheetEntry, ZoneId zoneId) {
        return new TimesheetEntryDto(timesheetEntry.project().name(),
                WorkPeriodDto.from(timesheetEntry.workPeriod(), zoneId));
    }
}
