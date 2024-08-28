package dev.nathanlively.application.clockin;

import dev.nathanlively.domain.TimesheetEntry;

public record ClockInResponse(String message, TimesheetEntry timesheetEntry) {
}
