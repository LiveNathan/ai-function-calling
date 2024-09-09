package dev.nathanlively.domain;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ResourcesTest {

    @Test
    void timesheetEntriesPerProjectAreUpdated_givenZeroEntries() {
        Resources resources = new Resources();
        Project project = Project.create("Project A");
        List<TimesheetEntry> expected = List.of();

        List<TimesheetEntry> actual = resources.timesheetEntries(project);

        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void timesheetEntriesPerProjectAreUpdated_givenOneEntry() {
        Resources resources = new Resources();
        Project project = Project.create("Project A");
        Resource resource = Resource.create(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", "nathanlively@gmail.com", null);
        resource.appendTimesheetEntry(project, Duration.ofHours(1), ZoneId.of("America/Chicago"));
        List<TimesheetEntry> expected = List.of(resource.timesheet().mostRecentEntry());

        List<TimesheetEntry> actual = resources.timesheetEntries(project);

        assertThat(actual)
                .isEqualTo(expected);
    }

}