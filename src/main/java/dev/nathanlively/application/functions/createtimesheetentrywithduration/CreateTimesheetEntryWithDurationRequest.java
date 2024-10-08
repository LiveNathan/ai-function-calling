package dev.nathanlively.application.functions.createtimesheetentrywithduration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record CreateTimesheetEntryWithDurationRequest(
        @JsonProperty(required = true) @JsonPropertyDescription("Project getName must match one of the currently available projects. If it doesn't, you can offer to create it.") String projectName,
        @JsonProperty(required = true) @JsonPropertyDescription("A time-based amount of time in the ISO-8601 duration format, such as PT30M for 30 minutes.") String timesheetEntryDuration,
        @JsonProperty(required = true) @JsonPropertyDescription("A time-zone ID, such as America/Chicago.") String zoneId
) {
}
