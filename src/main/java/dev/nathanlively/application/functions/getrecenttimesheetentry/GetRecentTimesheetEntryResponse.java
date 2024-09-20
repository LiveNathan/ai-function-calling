package dev.nathanlively.application.functions.getrecenttimesheetentry;

import dev.nathanlively.application.TimesheetEntryDto;

public record GetRecentTimesheetEntryResponse(String message, TimesheetEntryDto timesheetEntry) {
}
