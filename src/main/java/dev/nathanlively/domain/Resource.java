package dev.nathanlively.domain;

import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;

public final class Resource {
    private ResourceType resourceType;
    private JobTitle jobTitle;
    private String name;
    private String email;
    private Timesheet timeSheet;

    private Resource(ResourceType resourceType, JobTitle jobTitle, String name, String email,
                     Timesheet timeSheet, MyClock clock) {
        Objects.requireNonNull(resourceType, "ResourceType cannot be null");
        Objects.requireNonNull(jobTitle, "JobTitle cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");
        if (timeSheet == null) {
            switch (clock) {
                case MySystemClock systemClock -> this.timeSheet = Timesheet.withSystemClock(null);
                case FixedClock fixedClock -> this.timeSheet = Timesheet.withFixedClock(null, clock.now());
                case null, default -> throw new IllegalArgumentException("Unsupported clock type");
            }
        } else {
            this.timeSheet = timeSheet;
        }
        this.resourceType = resourceType;
        this.jobTitle = jobTitle;
        this.name = name;
        this.email = email;
    }

    public static Resource withSystemClock(ResourceType resourceType, JobTitle jobTitle, String name, String email, Timesheet timeSheet) {
        return new Resource(resourceType, jobTitle, name, email, timeSheet, new MySystemClock());
    }

    public static Resource withFixedClock(ResourceType resourceType, JobTitle jobTitle, String name, String email, Timesheet timeSheet, Instant fixedInstant) {
        return new Resource(resourceType, jobTitle, name, email, timeSheet, new FixedClock(fixedInstant));
    }

    public void appendTimesheetEntry(@NotNull TimesheetEntry timesheetEntry) {
        timeSheet.appendEntry(timesheetEntry);
    }

    public void appendTimesheetEntry(Project project, Duration duration, ZoneId zone) {
        timeSheet.appendEntryWithDuration(project, duration, zone);
    }

    public String email() {
        return email;
    }

    public Timesheet timesheet() {
        return timeSheet;
    }

}
