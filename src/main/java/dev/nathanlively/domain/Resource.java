package dev.nathanlively.domain;

import java.util.Objects;

public record Resource(ResourceType resourceType, JobTitle jobTitle, String name, String email, Timesheet timeSheet) {
    public Resource {
        Objects.requireNonNull(resourceType, "ResourceType cannot be null");
        Objects.requireNonNull(jobTitle, "JobTitle cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");
        if (timeSheet == null) {
            timeSheet = new Timesheet(null);
        }
    }

    public Resource appendTimesheetEntry(TimesheetEntry timesheetEntry) {
        Timesheet timesheet = timeSheet.appendEntry(timesheetEntry);
        return new Resource(resourceType, jobTitle, name, email, timesheet);
    }
}
