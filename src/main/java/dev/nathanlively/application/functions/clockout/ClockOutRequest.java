package dev.nathanlively.application.functions.clockout;

import java.time.Instant;

public record ClockOutRequest(Instant messageCreationTime) {
}
