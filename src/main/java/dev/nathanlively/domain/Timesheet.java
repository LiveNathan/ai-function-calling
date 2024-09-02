package dev.nathanlively.domain;

import dev.nathanlively.domain.exceptions.AlreadyClockedOutException;
import dev.nathanlively.domain.exceptions.NoTimesheetEntriesException;
import dev.nathanlively.domain.exceptions.OverlappingWorkPeriodException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Timesheet {
    private final List<TimesheetEntry> timeSheetEntries;

    public Timesheet(List<TimesheetEntry> timeSheetEntries) {
        if (timeSheetEntries == null) {
            timeSheetEntries = new ArrayList<>();
        }
        this.timeSheetEntries = timeSheetEntries;
    }

    public void appendEntry(TimesheetEntry timesheetEntry) {
        Objects.requireNonNull(timesheetEntry, "TimesheetEntry cannot be null");
        if (hasOverlappingPeriod(timesheetEntry)) {
            throw new OverlappingWorkPeriodException("Work periods cannot overlap.");
        }
        timeSheetEntries.add(timesheetEntry);
    }

    private boolean hasOverlappingPeriod(TimesheetEntry newEntry) {
        for (TimesheetEntry entry : timeSheetEntries) {
            if (entry.workPeriod().overlaps(newEntry.workPeriod())) {
                return true;
            }
        }
        return false;
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

    private void clockIn(Instant clockInTime, Project project) {
        if (!timeSheetEntries.isEmpty()) {
            TimesheetEntry recentEntry = mostRecentEntry();
            if (recentEntry.workPeriod().end() == null) {
                recentEntry.clockOut(clockInTime);
            }
        }
        TimesheetEntry newEntry = (project == null) ? TimesheetEntry.clockIn(clockInTime) : TimesheetEntry.clockIn(project, clockInTime);
        appendEntry(newEntry);
    }

    public void clockIn(Instant clockInTime) {
        clockIn(clockInTime, null);
    }

    public void clockInWithProject(Project project, Instant clockInTime) {
        Objects.requireNonNull(project, "Project cannot be null");
        clockIn(clockInTime, project);
    }

    public void clockOut(Instant clockOutTime) {
        TimesheetEntry recentEntry = mostRecentEntry();
        if (recentEntry.workPeriod().end() != null) {
            throw new AlreadyClockedOutException();
        }
        recentEntry.clockOut(clockOutTime);
    }
}
