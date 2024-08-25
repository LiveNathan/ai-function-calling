package dev.nathanlively.domain;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public final class TimesheetEntry {
    private final Instant clockIn;
    private final Instant clockOut;
    private final Duration duration;
    private final Project project;
    private WorkPeriod workPeriod;

    public TimesheetEntry(Instant clockIn, Instant clockOut, Duration duration, Project project) {
        Objects.requireNonNull(clockIn, "Clock-in time cannot be null.");
        if (clockOut != null && clockOut.isBefore(clockIn)) {
            throw new InvalidClockOutTimeException("Clock-out time cannot be before clock-in time.");
        }
        if (duration == null && clockOut != null) {
            duration = Duration.between(clockIn, clockOut);
        }
        this.clockIn = clockIn;
        this.clockOut = clockOut;
        this.duration = duration;
        this.project = project;
    }

    public TimesheetEntry clockOutAndSetDuration(Instant clockOutTime) {
        if (clockOutTime.isBefore(clockIn)) {
            throw new InvalidClockOutTimeException("Clock-out time cannot be before clock-in time.");
        }
        Duration computedDuration = Duration.between(clockIn, clockOutTime);
        return new TimesheetEntry(clockIn, clockOutTime, computedDuration, project);
    }

    public TimesheetEntry appendProject(Project project) {
        return new TimesheetEntry(clockIn, clockOut, duration, project);
    }

    public Instant clockIn() {
        return clockIn;
    }

    public Instant clockOut() {
        return clockOut;
    }

    public Duration duration() {
        return duration;
    }

    public Project project() {
        return project;
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
