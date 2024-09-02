package dev.nathanlively.application.functions.createtimesheetentry;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.time.LocalDateTime;

public record CreateTimesheetEntryRequest(
        @JsonProperty(required = true) @JsonPropertyDescription("Project name must match one of the currently available projects.") String projectName,
        @JsonProperty(required = true) @JsonPropertyDescription("A date-time without a time-zone in the ISO-8601 calendar system, such as 2007-12-03T10:15:30.") LocalDateTime timesheetEntryStart,
        @JsonProperty(required = true) @JsonPropertyDescription("A date-time without a time-zone in the ISO-8601 calendar system, such as 2007-12-03T10:15:30.") LocalDateTime timesheetEntryEnd,
        @JsonProperty(required = true) @JsonPropertyDescription("A time-zone ID, such as America/Chicago.") String zoneId
) {
}
