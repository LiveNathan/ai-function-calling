package dev.nathanlively.domain;

import java.util.List;

public record Project(String name, List<TimesheetEntry> timeSheetEntries) {
}
