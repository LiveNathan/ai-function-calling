package dev.nathanlively.application.functions.clockin;

import dev.nathanlively.domain.TimesheetEntry;

public record ClockInResponse(String message, TimesheetEntry timesheetEntry) {
}
