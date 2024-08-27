package dev.nathanlively.application.updateproject;

import dev.nathanlively.domain.TimesheetEntry;

public record UpdateProjectResponse(String message, TimesheetEntry timesheetEntry) {
}
