package dev.nathanlively.application.functions.clockout;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.time.LocalDateTime;

public record ClockOutRequest(
        @JsonProperty(required = true) LocalDateTime clockOutTime,
        @JsonProperty(required = true) @JsonPropertyDescription("The user's timezone.") String timezoneId) {
}
