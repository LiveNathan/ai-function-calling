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

//    public ResourceType resourceType() {
//        return resourceType;
//    }

//    public JobTitle jobTitle() {
//        return jobTitle;
//    }

//    public String name() {
//        return name;
//    }

    public String email() {
        return email;
    }

    public Timesheet timeSheet() {
        return timeSheet;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (obj == this) return true;
//        if (obj == null || obj.getClass() != this.getClass()) return false;
//        var that = (Resource) obj;
//        return Objects.equals(this.resourceType, that.resourceType) &&
//                Objects.equals(this.jobTitle, that.jobTitle) &&
//                Objects.equals(this.name, that.name) &&
//                Objects.equals(this.email, that.email) &&
//                Objects.equals(this.timeSheet, that.timeSheet);
//    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(resourceType, jobTitle, name, email, timeSheet);
//    }

//    @Override
//    public String toString() {
//        return "Resource[" +
//                "resourceType=" + resourceType + ", " +
//                "jobTitle=" + jobTitle + ", " +
//                "name=" + name + ", " +
//                "email=" + email + ", " +
//                "timeSheet=" + timeSheet + ']';
//    }

}
