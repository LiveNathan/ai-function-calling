package dev.nathanlively.application;

import dev.nathanlively.domain.TimesheetEntry;

public record TimesheetEntryDto(String projectName, WorkPeriodDto workPeriodDto) {
    public static TimesheetEntryDto from(TimesheetEntry timesheetEntry) {
        return new TimesheetEntryDto(timesheetEntry.project().name(),
                WorkPeriodDto.from(timesheetEntry.workPeriod()));
    }
}
