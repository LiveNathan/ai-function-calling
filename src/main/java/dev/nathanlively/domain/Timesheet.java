package dev.nathanlively.domain;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public final class Timesheet {
    private List<TimesheetEntry> timeSheetEntries;

    public Timesheet(List<TimesheetEntry> timeSheetEntries) {
        if (timeSheetEntries == null) {
            timeSheetEntries = new java.util.ArrayList<>();
        }
        this.timeSheetEntries = timeSheetEntries;
    }

    public void appendEntry(@NotNull TimesheetEntry timesheetEntry) {
        timeSheetEntries.add(timesheetEntry);
    }

    public List<TimesheetEntry> timeSheetEntries() {
        return List.copyOf(timeSheetEntries);
    }

    public TimesheetEntry mostRecentEntry() {
        if (timeSheetEntries.isEmpty()) {
            throw new IllegalStateException("No timesheet entries found");
        }
        return timeSheetEntries.getLast();
    }
}
