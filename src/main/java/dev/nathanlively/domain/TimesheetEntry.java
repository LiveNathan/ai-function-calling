package dev.nathanlively.domain;

import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.Instant;
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
    public String toString() {
        return "TimesheetEntry{" +
                "project=" + project +
                ", workPeriod=" + workPeriod +
                '}';
    }
}
