package dev.nathanlively.application;

import dev.nathanlively.domain.Project;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimesheetEntryCreator {
    public static Result<TimesheetEntry> createAndAppendEntry(Resource resource, Project project, LocalDateTime start, LocalDateTime end, String zone) {
        TimesheetEntry entry = TimesheetEntry.from(project, start, end, ZoneId.of(zone));
        resource.appendTimesheetEntry(entry);
        return Result.success(entry);
    }

    public static Result<TimesheetEntry> createAndAppendEntry(Resource resource, Project project, Duration duration, String zone) {
        resource.appendTimesheetEntry(project, duration, ZoneId.of(zone));
        return Result.success(resource.timesheet().mostRecentEntry());
    }
}
