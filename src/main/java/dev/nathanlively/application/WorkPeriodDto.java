package dev.nathanlively.application;

import dev.nathanlively.domain.WorkPeriod;

import java.time.Duration;
import java.time.Instant;

public record WorkPeriodDto(Instant start, Instant end, Duration duration) {
    public static WorkPeriodDto from(WorkPeriod workPeriod) {
        Duration duration = null;
        if (workPeriod.end() != null) {
            duration = workPeriod.getDuration();
        }
        return new WorkPeriodDto(workPeriod.start(), workPeriod.end(), duration);
    }
}
