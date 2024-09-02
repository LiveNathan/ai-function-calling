package dev.nathanlively.application.functions.clockin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.time.Instant;

public record ClockInRequest(
        @JsonProperty(required = true) @JsonPropertyDescription("Project name must match one of the currently available projects. If it doesn't, you can offer to create it.") String projectName,
        Instant messageCreationTime
) {
}
