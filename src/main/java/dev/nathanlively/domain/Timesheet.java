package dev.nathanlively.domain;

import java.util.ArrayList;
import java.util.List;

public final class Timesheet {
    private final List<TimesheetEntry> timeSheetEntries;

    public Timesheet(List<TimesheetEntry> timeSheetEntries) {
        timeSheetEntries = timeSheetEntries == null ? List.of() : List.copyOf(timeSheetEntries);
        this.timeSheetEntries = timeSheetEntries;
    }

    public Timesheet appendEntry(TimesheetEntry timesheetEntry) {
        List<TimesheetEntry> updatedEntries = new ArrayList<>(this.timeSheetEntries);
        updatedEntries.add(timesheetEntry);
        return new Timesheet(updatedEntries);
    }

    public List<TimesheetEntry> timeSheetEntries() {
        return List.copyOf(timeSheetEntries);
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (obj == this) return true;
//        if (obj == null || obj.getClass() != this.getClass()) return false;
//        var that = (Timesheet) obj;
//        return Objects.equals(this.timeSheetEntries, that.timeSheetEntries);
//    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(timeSheetEntries);
//    }

//    @Override
//    public String toString() {
//        return "Timesheet[" +
//                "timeSheetEntries=" + timeSheetEntries + ']';
//    }

}
