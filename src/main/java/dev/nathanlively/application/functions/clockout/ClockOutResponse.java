package dev.nathanlively.application.functions.clockout;

import dev.nathanlively.domain.TimesheetEntry;

public record ClockOutResponse(String message, TimesheetEntry timesheetEntry) {
}
