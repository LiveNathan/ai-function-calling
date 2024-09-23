package dev.nathanlively.application.functions.clockout;

import dev.nathanlively.application.TimesheetEntryDto;

public record ClockOutResponse(String message, TimesheetEntryDto timesheetEntry) {
}
