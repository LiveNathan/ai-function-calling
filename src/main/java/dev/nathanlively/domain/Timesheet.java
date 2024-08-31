package dev.nathanlively.domain;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class Timesheet {
    private List<TimesheetEntry> timeSheetEntries;

    public Timesheet(List<TimesheetEntry> timeSheetEntries) {
        if (timeSheetEntries == null) {
            timeSheetEntries = new ArrayList<>();
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

    public void clockIn(Instant clockInTime) {
        if (!timeSheetEntries.isEmpty()) {
            TimesheetEntry mostRecentEntry = mostRecentEntry();
            if (mostRecentEntry.workPeriod().end() == null) {
                throw new IllegalStateException("Cannot clock in. The previous entry has not been clocked out.");
            }
        }
        TimesheetEntry newEntry = TimesheetEntry.clockIn(clockInTime);
        appendEntry(newEntry);
    }
}
