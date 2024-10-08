package dev.nathanlively.domain;

import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class TimesheetEntry {
    private Project project;
    private @NotNull WorkPeriod workPeriod;

    private TimesheetEntry(Project project, WorkPeriod workPeriod) {
        Objects.requireNonNull(workPeriod, "WorkPeriod cannot be null");
        this.project = project;
        this.workPeriod = workPeriod;
    }

    private TimesheetEntry(WorkPeriod workPeriod) {
        this(null, workPeriod);
    }

    public static TimesheetEntry clockIn(Instant clockInTime) {
        return new TimesheetEntry(WorkPeriod.startAt(clockInTime));
    }

    public static TimesheetEntry clockIn(Project project, Instant clockInTime) {
        return new TimesheetEntry(project, WorkPeriod.startAt(clockInTime));
    }

    public static TimesheetEntry from(Project project, LocalDateTime start, LocalDateTime end,
                                      ZoneId zone) {
        Instant startInstant = start.atZone(zone).toInstant();
        Instant endInstant = end.atZone(zone).toInstant();
        return TimesheetEntry.from(project, startInstant, endInstant);
    }

    public static TimesheetEntry from(Project project, Instant start, Instant end) {
        TimesheetEntry entry = clockIn(project, start);
        entry.clockOut(end);
        return entry;
    }

    public void clockOut(Instant clockOutTime) {
        workPeriod.setEnd(clockOutTime);
    }

    public Project project() {
        return project;
    }

    public void setProject(Project project) {
        Objects.requireNonNull(project, "Project cannot be null");
        this.project = project;
    }

    public Duration duration() {
        return workPeriod.getDuration();
    }

    public WorkPeriod workPeriod() {
        return new WorkPeriod(workPeriod.start(), workPeriod.end());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimesheetEntry that = (TimesheetEntry) o;
        return Objects.equals(project, that.project) && Objects.equals(workPeriod, that.workPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project, workPeriod);
    }

    @Override
    public String toString() {
        return "TimesheetEntry{" +
                "project=" + project +
                ", workPeriod=" + workPeriod +
                '}';
    }
}
