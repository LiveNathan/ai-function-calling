package dev.nathanlively.domain;

import dev.nathanlively.domain.exceptions.AlreadyClockedOutException;
import dev.nathanlively.domain.exceptions.NoTimesheetEntriesException;
import dev.nathanlively.domain.exceptions.OverlappingWorkPeriodException;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Timesheet {
    private final List<TimesheetEntry> timeSheetEntries;
    private final MyClock clock;

    private Timesheet(List<TimesheetEntry> timeSheetEntries, MyClock clock) {
        if (timeSheetEntries == null) {
            timeSheetEntries = new ArrayList<>();
        }
        this.timeSheetEntries = timeSheetEntries;
        this.clock = Objects.requireNonNull(clock);
    }

    public static Timesheet withSystemClock(List<TimesheetEntry> timeSheetEntries) {
        return new Timesheet(timeSheetEntries, new MySystemClock());
    }

    public static Timesheet withFixedClock(List<TimesheetEntry> timeSheetEntries, Instant fixedInstant) {
        return new Timesheet(timeSheetEntries, new FixedClock(fixedInstant));
    }

    public void appendEntry(TimesheetEntry timesheetEntry) {
        Objects.requireNonNull(timesheetEntry, "TimesheetEntry cannot be null");
        if (hasOverlappingPeriod(timesheetEntry)) {
            throw new OverlappingWorkPeriodException("Work periods cannot overlap.");
        }
        timeSheetEntries.add(timesheetEntry);
    }

    public void appendEntryWithDuration(Project project, Duration duration, ZoneId zone) {
        Objects.requireNonNull(project, "Project cannot be null");
        Objects.requireNonNull(duration, "Duration cannot be null");

        Instant start = calculateNextAvailableSlot(zone);
        Instant end = start.plus(duration);

        TimesheetEntry newEntry = TimesheetEntry.from(project, start, end);
        appendEntry(newEntry);
    }

    Instant calculateNextAvailableSlot(ZoneId zone) {
        if (timeSheetEntries.isEmpty()) {
            Instant now = clock.now();
            LocalDate today = LocalDate.ofInstant(now, zone);
            int defaultStartHour = 9;
            LocalDateTime todayAt9 = today.atTime(defaultStartHour, 0);
            return todayAt9.atZone(zone).toInstant();
        } else {
            TimesheetEntry lastEntry = mostRecentEntry();
            Instant lastEnd = lastEntry.workPeriod().end();
            return (lastEnd != null) ? lastEnd : lastEntry.workPeriod().start();
        }
    }

    private boolean hasOverlappingPeriod(TimesheetEntry newEntry) {
        return timeSheetEntries.stream()
                .anyMatch(entry -> entry.workPeriod().overlaps(newEntry.workPeriod()));
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

    public List<TimesheetEntry> entriesByProject(Project projectA) {
        Objects.requireNonNull(projectA, "Project cannot be null");
        return timeSheetEntries.stream()
                .filter(entry -> projectA.equals(entry.project()))
                .collect(Collectors.toList());
    }
}
