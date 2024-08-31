package dev.nathanlively.domain;

import dev.nathanlively.domain.exceptions.InvalidClockOutTimeException;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class WorkPeriod {
    private final @NotNull Instant start;
    private Instant end;

    public WorkPeriod(Instant start, Instant end) {
        Objects.requireNonNull(start, "Start time cannot be null.");
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

    private static void validateEndTime(Instant start, Instant end) {
        if (end != null && end.isBefore(start)) {
            throw new InvalidClockOutTimeException("End time cannot be before start time.");
        }
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
