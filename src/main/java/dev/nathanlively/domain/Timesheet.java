package dev.nathanlively.domain;

import java.util.List;

public record Timesheet(List<TimesheetEntry> timeSheetEntries) {
}
