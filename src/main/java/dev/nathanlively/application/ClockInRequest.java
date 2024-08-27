package dev.nathanlively.application;

import java.time.Instant;

public record ClockInRequest(String projectName, Instant clockInTime) {
}
