package dev.nathanlively.application.functions.clockin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.time.LocalDateTime;

public record ClockInRequest(
        @JsonProperty(required = true) @JsonPropertyDescription("Project name must match one of the currently available projects. If it doesn't, you can offer to create it.") String projectName,
        @JsonProperty(required = true) LocalDateTime clockInTime,
        @JsonProperty(required = true) @JsonPropertyDescription("The user's timezone.") String timezoneId) {
}
