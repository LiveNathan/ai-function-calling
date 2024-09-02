package dev.nathanlively.application.functions.createtimesheetentry;

import dev.nathanlively.domain.TimesheetEntry;

public record CreateTimesheetEntryResponse(String message, TimesheetEntry timesheetEntry) {
}
