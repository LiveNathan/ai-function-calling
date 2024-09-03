package dev.nathanlively.application.functions.createtimesheetentrywithduration;

import dev.nathanlively.domain.TimesheetEntry;

public record CreateTimesheetEntryWithDurationResponse(String message, TimesheetEntry timesheetEntry) {
}
