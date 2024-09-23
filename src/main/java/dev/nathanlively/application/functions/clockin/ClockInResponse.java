package dev.nathanlively.application.functions.clockin;

import dev.nathanlively.application.TimesheetEntryDto;

public record ClockInResponse(String message, TimesheetEntryDto timesheetEntry) {
}
