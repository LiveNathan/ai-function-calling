package dev.nathanlively.application;

import dev.nathanlively.domain.TimesheetEntry;

public record ClockInResponse(String message, TimesheetEntry timesheetEntry) {
}
