package dev.nathanlively.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public final class Resource {
    private @NotNull ResourceType resourceType;
    private @NotNull JobTitle jobTitle;
    private @NotBlank String name;
    private @NotBlank String email;
    private Timesheet timeSheet;

    public Resource(ResourceType resourceType, JobTitle jobTitle, String name, String email, Timesheet timeSheet) {
        Objects.requireNonNull(resourceType, "ResourceType cannot be null");
        Objects.requireNonNull(jobTitle, "JobTitle cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");
        if (timeSheet == null) {
            timeSheet = new Timesheet(null);
        }
        this.resourceType = resourceType;
        this.jobTitle = jobTitle;
        this.name = name;
        this.email = email;
        this.timeSheet = timeSheet;
    }

    public void appendTimesheetEntry(@NotNull TimesheetEntry timesheetEntry) {
        timeSheet.appendEntry(timesheetEntry);
    }

    public String email() {
        return email;
    }

    public Timesheet timeSheet() {
        return timeSheet;
    }

}
