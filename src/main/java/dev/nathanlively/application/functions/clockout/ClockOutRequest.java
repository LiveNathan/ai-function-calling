package dev.nathanlively.application.functions.clockout;

import java.time.LocalDateTime;

public record ClockOutRequest(LocalDateTime messageCreationTime, String zoneId) {
}
