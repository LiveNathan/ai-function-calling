package dev.nathanlively.application;

import dev.nathanlively.domain.WorkPeriod;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public record WorkPeriodDto(LocalDateTime start, LocalDateTime end, Duration duration) {

    public static WorkPeriodDto from(WorkPeriod workPeriod, ZoneId zoneId) {
        Objects.requireNonNull(workPeriod, "WorkPeriod cannot be null");
        Objects.requireNonNull(zoneId, "zoneId cannot be null.");

        Duration duration = (workPeriod.end() != null) ? workPeriod.getDuration() : null;

        LocalDateTime start = LocalDateTime.ofInstant(workPeriod.start(), zoneId);
        LocalDateTime end = null;
        if (workPeriod.end() != null) {
            end = LocalDateTime.ofInstant(workPeriod.end(), zoneId);
        }

        return new WorkPeriodDto(start, end, duration);
    }
}
