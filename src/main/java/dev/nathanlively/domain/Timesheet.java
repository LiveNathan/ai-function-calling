package dev.nathanlively.domain;

import dev.nathanlively.domain.exceptions.AlreadyClockedOutException;
import dev.nathanlively.domain.exceptions.IncompleteEntryException;
import dev.nathanlively.domain.exceptions.NoTimesheetEntriesException;
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
            throw new NoTimesheetEntriesException();
        }
        return timeSheetEntries.getLast();
    }

    private void clockIn(Instant clockInTime, Project projectA) {
        if (!timeSheetEntries.isEmpty()) {
            if (mostRecentEntry().workPeriod().end() == null) {
                throw new IncompleteEntryException();
            }
        }
        TimesheetEntry newEntry = null;
        if (projectA == null) {
            newEntry = TimesheetEntry.clockIn(clockInTime);
        } else {
            newEntry = TimesheetEntry.clockIn(projectA, clockInTime);
        }
        appendEntry(newEntry);
    }

    public void clockIn(Instant clockInTime) {
        clockIn(clockInTime, null);
    }

    public void clockInWithProject(Project projectA, Instant clockInTime) {
        clockIn(clockInTime, projectA);
    }

    public void clockOut(Instant clockOutTime) {
        TimesheetEntry recentEntry = mostRecentEntry();
        if (recentEntry.workPeriod().end() != null) {
            throw new AlreadyClockedOutException();
        }
        recentEntry.clockOut(clockOutTime);
    }
}
