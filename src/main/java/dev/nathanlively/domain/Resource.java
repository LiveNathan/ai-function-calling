package dev.nathanlively.domain;

import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;

public final class Resource extends Named {
    private final ResourceType resourceType;
    private final JobTitle jobTitle;
    private final String email;
    private Timesheet timeSheet;

    private Resource(ResourceType resourceType, JobTitle jobTitle, String name, String email, Timesheet timeSheet, MyClock clock) {
        super(name);
        Objects.requireNonNull(resourceType, "ResourceType cannot be null");
        Objects.requireNonNull(jobTitle, "JobTitle cannot be null");
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
        this.email = email;
    }

    public static Resource create(ResourceType resourceType, JobTitle jobTitle, String name, String email, Timesheet timeSheet) {
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

    // hashCode, equals and toString overridden for a better object comparison and representation
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Resource resource = (Resource) obj;
        return resourceType == resource.resourceType &&
                jobTitle == resource.jobTitle &&
                Objects.equals(email, resource.email) &&
                Objects.equals(timeSheet, resource.timeSheet) &&
                Objects.equals(name(), resource.name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceType, jobTitle, name(), email, timeSheet);
    }

    @Override
    public String toString() {
        return "Resource[" +
                "resourceType=" + resourceType +
                ", jobTitle=" + jobTitle +
                ", getName=" + name() +
                ", email=" + email +
                ", timeSheet=" + timeSheet +
                ']';
    }
}
