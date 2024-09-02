package dev.nathanlively.domain;

import dev.nathanlively.domain.exceptions.InvalidClockOutTimeException;
import dev.nathanlively.domain.exceptions.InvalidWorkPeriodException;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class WorkPeriod {
    private final @NotNull Instant start;
    private Instant end;

    public WorkPeriod(Instant start, Instant end) {
        Objects.requireNonNull(start, "Start time cannot be null.");
        validateFutureTime(start, "Start time cannot be in the future.");
        validateEndTime(start, end);
        this.start = start;
        this.end = end;
    }

    private WorkPeriod(Instant start) {
        this(start, null);
    }

    public static WorkPeriod startAt(Instant startTime) {
        return new WorkPeriod(startTime);
    }

    public Instant start() {
        return start;
    }

    public Instant end() {
        return end;
    }

    public void setEnd(Instant end) {
        validateEndTime(start, end);
        this.end = end;
    }

    public Duration getDuration() {
        if (end == null) {
            throw new InvalidClockOutTimeException("Cannot calculate duration without end time.");
        }
        return Duration.between(start, end);
    }

    private static void validateFutureTime(Instant time, String message) {
        if (time.isAfter(Instant.now())) {
            throw new InvalidWorkPeriodException(message);
        }
    }

    private static void validateEndTime(Instant start, Instant end) {
        if (end != null) {
            validateFutureTime(end, "End time cannot be in the future.");
            if (end.isBefore(start)) {
                throw new InvalidClockOutTimeException("End time cannot be before start time.");
            }
        }
    }

    public boolean overlaps(WorkPeriod newPeriod) {
        Objects.requireNonNull(newPeriod, "New work period cannot be null");

        if (newPeriod.start().isBefore(this.start)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkPeriod that = (WorkPeriod) o;
        return start.equals(that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "WorkPeriod{" +
                "start=" + start +
                ", end=" + (end != null ? end : "not set") +
                '}';
    }
}
