package dev.nathanlively.application.functions.getrecenttimesheetentry;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record GetRecentTimesheetEntryRequest(
        @JsonProperty(required = false) @JsonPropertyDescription("Unused parameter.") String unused) {
}
