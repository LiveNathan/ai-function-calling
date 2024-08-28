package dev.nathanlively.application.functions.updateproject;

import dev.nathanlively.domain.TimesheetEntry;

public record UpdateProjectResponse(String message, TimesheetEntry timesheetEntry) {
}
