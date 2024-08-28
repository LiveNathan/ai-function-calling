package dev.nathanlively.application.functions.clockin;

import jakarta.annotation.Nullable;

import java.time.Instant;

public record ClockInRequest(@Nullable String projectName, Instant messageCreationTime) {
}
