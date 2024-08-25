package dev.nathanlively.domain;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class TimesheetEntry {
    private Project project;
    private WorkPeriod workPeriod;

    public TimesheetEntry(Project project, WorkPeriod workPeriod) {
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

    public void clockOut(Instant clockOutTime) {
        workPeriod.setEnd(clockOutTime);
    }

//    public Project project() {
//        return project;
//    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Duration duration() {
        return workPeriod.getDuration();
    }

    public WorkPeriod workPeriod() {
        return workPeriod;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (obj == this) return true;
//        if (obj == null || obj.getClass() != this.getClass()) return false;
//        var that = (TimesheetEntry) obj;
//        return Objects.equals(this.clockIn, that.clockIn) &&
//                Objects.equals(this.clockOut, that.clockOut) &&
//                Objects.equals(this.duration, that.duration) &&
//                Objects.equals(this.project, that.project);
//    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(clockIn, clockOut, duration, project);
//    }

//    @Override
//    public String toString() {
//        return "TimesheetEntry[" +
//                "clockIn=" + clockIn + ", " +
//                "clockOut=" + clockOut + ", " +
//                "duration=" + duration + ", " +
//                "project=" + project + ']';
//    }
}
