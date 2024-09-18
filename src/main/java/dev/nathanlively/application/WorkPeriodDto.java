package dev.nathanlively.application;

import dev.nathanlively.domain.WorkPeriod;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;

public record WorkPeriodDto(LocalDateTime start, LocalDateTime end, Duration duration) {

    public static WorkPeriodDto from(WorkPeriod workPeriod, ZoneId zoneId) {
        Objects.requireNonNull(workPeriod, "WorkPeriod cannot be null");

        Duration duration = (workPeriod.end() != null) ? workPeriod.getDuration() : null;

        ZoneId effectiveZoneId = (zoneId != null) ? zoneId : ZoneOffset.UTC;

        LocalDateTime start = LocalDateTime.ofInstant(workPeriod.start(), effectiveZoneId);
        LocalDateTime end = null;
        if (workPeriod.end() != null) {
            end = LocalDateTime.ofInstant(workPeriod.end(), effectiveZoneId);
        }

        return new WorkPeriodDto(start, end, duration);
    }
}
