package dev.nathanlively.domain;

public record Resource(ResourceType resourceType, JobTitle jobTitle, String name, String email, Timesheet timeSheet) {
}
