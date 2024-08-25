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
