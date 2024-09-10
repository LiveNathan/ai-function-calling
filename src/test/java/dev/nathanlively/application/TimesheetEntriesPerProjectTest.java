package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TimesheetEntriesPerProjectTest {
    @Test
    void with() throws Exception {
        ResourceRepository resourceRepository = InMemoryResourceRepository.createEmpty();
        ProjectRepository projectRepository = InMemoryProjectRepository.createEmpty();
        TimesheetEntriesPerProject service = new TimesheetEntriesPerProject(resourceRepository, projectRepository);
        Project project = Project.create("Project A");
        projectRepository.save(project);
        ZoneId ZONE_ID = ZoneId.of("America/Chicago");
        Instant fixedInstant = LocalDate.of(2024, 3, 15).atStartOfDay(ZONE_ID).toInstant();
        Resource resource = Resource.withFixedClock(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", "nathanlively@gmail.com", null, fixedInstant);
        resourceRepository.save(resource);
        resource.appendTimesheetEntry(project, Duration.ofHours(1), ZoneId.of("America/Chicago"));
        List<TimesheetEntry> expected = new ArrayList<>();
        expected.add(resource.timesheet().mostRecentEntry());

        List<TimesheetEntry> actual = service.with(project.name());

        assertThat(actual)
                .isEqualTo(expected);
    }
}