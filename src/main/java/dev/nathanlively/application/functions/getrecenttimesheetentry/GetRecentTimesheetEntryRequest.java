package dev.nathanlively.application.functions.getrecenttimesheetentry;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetRecentTimesheetEntryRequest(@JsonProperty(required = true) String username) { }
