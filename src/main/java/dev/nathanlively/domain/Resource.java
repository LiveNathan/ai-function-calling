package dev.nathanlively.domain;

import java.util.Objects;

public record Resource(ResourceType resourceType, JobTitle jobTitle, String name, String email, Timesheet timeSheet) {
    public Resource {
        Objects.requireNonNull(resourceType, "ResourceType cannot be null");
        Objects.requireNonNull(jobTitle, "JobTitle cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");
    }
}
