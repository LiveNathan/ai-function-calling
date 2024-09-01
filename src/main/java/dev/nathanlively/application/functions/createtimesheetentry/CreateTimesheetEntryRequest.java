package dev.nathanlively.application.functions.createtimesheetentry;

import java.time.LocalDateTime;

public record CreateTimesheetEntryRequest(String projectName, LocalDateTime timesheetEntryStart, LocalDateTime timesheetEntryEnd, String zoneId) {
}
