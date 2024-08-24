package dev.nathanlively.domain;

import java.util.ArrayList;
import java.util.List;

public record Timesheet(List<TimesheetEntry> timeSheetEntries) {

    public Timesheet {
        timeSheetEntries = timeSheetEntries == null ? List.of() : List.copyOf(timeSheetEntries);
    }

    public Timesheet appendEntry(TimesheetEntry timesheetEntry) {
        List<TimesheetEntry> updatedEntries = new ArrayList<>(this.timeSheetEntries);
        updatedEntries.add(timesheetEntry);
        return new Timesheet(updatedEntries);
    }
}
