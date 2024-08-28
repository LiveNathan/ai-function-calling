package dev.nathanlively.application.clockin;

import jakarta.annotation.Nullable;

import java.time.Instant;

public record ClockInRequest(@Nullable String projectName, Instant messageCreationTime) {
}
