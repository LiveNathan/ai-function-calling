package dev.nathanlively.application.clockin;

import java.time.Instant;

public record ClockInRequest(String projectName, Instant clockInTime) {
}
