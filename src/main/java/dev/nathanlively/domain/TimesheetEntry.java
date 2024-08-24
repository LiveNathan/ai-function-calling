package dev.nathanlively.domain;

import java.time.Duration;
import java.time.Instant;

public record TimesheetEntry(Instant clockIn, Instant clockOut, Duration duration, Project project) {
    public TimesheetEntry clockOutAndSetDuration(Instant clockOutTime) {
        if (clockOutTime.isBefore(clockIn)) {
            throw new InvalidClockOutTimeException("Clock-out time cannot be before clock-in time.");
        }
        Duration computedDuration = Duration.between(clockIn, clockOutTime);
        return new TimesheetEntry(clockIn, clockOutTime, computedDuration, project);
    }
}
