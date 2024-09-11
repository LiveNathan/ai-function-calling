package dev.nathanlively.domain;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ResourcesTest {

    @Test
    void timesheetEntriesPerProjectAreUpdated_givenZeroEntriesByProject() {
        Resources resources = new Resources();
        Project project = Project.create("Project A");
        List<TimesheetEntry> expected = List.of();

        List<TimesheetEntry> actual = resources.timesheetEntriesByProject(project);

        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void timesheetEntriesByProjectPerProjectAreUpdated_givenOneEntry() {
        Resources resources = new Resources();
        Project project = Project.create("Project A");
        ZoneId ZONE_ID = ZoneId.of("America/Chicago");
        Instant fixedInstant = LocalDate.of(2024, 3, 15).atStartOfDay(ZONE_ID).toInstant();
        Resource resource = Resource.withFixedClock(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", "nathanlively@gmail.com", null, fixedInstant);
        resources.add(resource);
        resource.appendTimesheetEntry(project, Duration.ofHours(1), ZoneId.of("America/Chicago"));
        List<TimesheetEntry> expected = List.of(resource.timesheet().mostRecentEntry());

        List<TimesheetEntry> actual = resources.timesheetEntriesByProject(project);

        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void totalTimesheetEntryHoursByProject() {
        Resources resources = new Resources();
        Project project = Project.create("Project A");
        ZoneId ZONE_ID = ZoneId.of("America/Chicago");
        Instant fixedInstant = LocalDate.of(2024, 3, 15).atStartOfDay(ZONE_ID).toInstant();
        Resource resource = Resource.withFixedClock(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", "nathanlively@gmail.com", null, fixedInstant);
        resources.add(resource);
        resource.appendTimesheetEntry(project, Duration.ofHours(1), ZoneId.of("America/Chicago"));
        resource.appendTimesheetEntry(project, Duration.ofMinutes(20), ZoneId.of("America/Chicago"));
        float expected = 1.33f;

        float actual = resources.totalTimesheetEntryHours(project);

        assertThat(actual)
                .isEqualTo(expected);
    }

}