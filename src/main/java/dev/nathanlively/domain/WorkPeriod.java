package dev.nathanlively.domain;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class WorkPeriod {
    private final Instant start;
    private Instant end;

    public WorkPeriod(Instant start, Instant end) {
        Objects.requireNonNull(start, "Start time cannot be null.");
        if (end != null && end.isBefore(start)) {
            throw new InvalidClockOutTimeException("End time cannot be before start time.");
        }
        this.start = start;
        this.end = end;
    }

//    public Instant getStart() {
//        return start;
//    }

//    public Instant getEnd() {
//        return end;
//    }

    public Duration getDuration() {
        if (end == null) {
            return null;
        }
        return Duration.between(start, end);
    }

    public WorkPeriod withEnd(Instant endTime) {
        return new WorkPeriod(start, endTime);
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        WorkPeriod that = (WorkPeriod) o;
//        return start.equals(that.start) && Objects.equals(end, that.end);
//    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(start, end);
//    }

//    @Override
//    public String toString() {
//        return "WorkPeriod{" +
//                "start=" + start +
//                ", end=" + (end != null ? end : "not set") +
//                '}';
//    }
}
