package dev.nathanlively.application.functions.getrecenttimesheetentry;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.time.ZoneId;

public record GetRecentTimesheetEntryRequest(
        @JsonProperty(required = true) @JsonPropertyDescription("The user's email address.") String email,
        @JsonProperty(required = true) @JsonPropertyDescription("The user's timezone.") ZoneId timezone) { }
