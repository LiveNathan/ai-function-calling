package dev.nathanlively.application.functions.getrecenttimesheetentry;

import dev.nathanlively.domain.TimesheetEntry;

public record GetRecentTimesheetEntryResponse(String message, TimesheetEntry timesheetEntry) {
}
