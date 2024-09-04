package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.*;
import org.junit.jupiter.api.Test;

import java.time.*;

import static dev.nathanlively.application.ResultAssertions.assertThat;

class CreateTimesheetEntryServiceTest {
    private static final ZoneId ZONE_ID = ZoneId.of("America/Chicago");

    @Test
    void from() {
        ResourceRepository resourceRepository = InMemoryResourceRepository.createEmpty();
        ProjectRepository projectRepository = InMemoryProjectRepository.createEmpty();
        String resourceEmail = "nathanlively@gmail.com";
        Resource resource = Resource.withSystemClock(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", resourceEmail, null);
        String projectName = "Project A";
        Project project = Project.create(projectName);
        resourceRepository.save(resource);
        projectRepository.save(project);
        CreateTimesheetEntryService service = new CreateTimesheetEntryService(resourceRepository, projectRepository);
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 8, 0);
        LocalDateTime end = LocalDateTime.of(2024, 3, 15, 9, 0);
        ZoneId zone = ZoneId.of("America/Chicago");
        Instant expectedStartInstant = start.atZone(zone).toInstant();
        Instant expectedEndInstant = end.atZone(zone).toInstant();
        TimesheetEntry expected = TimesheetEntry.clockIn(project, expectedStartInstant);
        expected.clockOut(expectedEndInstant);

        Result<TimesheetEntry> actual = service.from(resourceEmail, projectName, start, end, zone.toString());

        assertThat(actual).isSuccess();
        assertThat(actual.failureMessages()).isEmpty();
        assertThat(actual).successValues().contains(expected);
    }

    @Test
    void from_withDuration() {
        ResourceRepository resourceRepository = InMemoryResourceRepository.createEmpty();
        ProjectRepository projectRepository = InMemoryProjectRepository.createEmpty();
        String resourceEmail = "nathanlively@gmail.com";
        Instant fixedInstant = LocalDate.of(2024, 3, 15).atStartOfDay(ZONE_ID).toInstant();
        Resource resource = Resource.withFixedClock(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", resourceEmail, null, fixedInstant);
        String projectName = "Project A";
        Project project = Project.create(projectName);
        resourceRepository.save(resource);
        projectRepository.save(project);
        CreateTimesheetEntryService service = new CreateTimesheetEntryService(resourceRepository, projectRepository);
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 9, 0);
        LocalDateTime end = LocalDateTime.of(2024, 3, 15, 10, 0);
        ZoneId zone = ZoneId.of("America/Chicago");
        Duration duration = Duration.ofHours(1);

        Instant expectedStartInstant = start.atZone(zone).toInstant();
        Instant expectedEndInstant = end.atZone(zone).toInstant();
        TimesheetEntry expected = TimesheetEntry.clockIn(project, expectedStartInstant);
        expected.clockOut(expectedEndInstant);

        Result<TimesheetEntry> actual = service.from(resourceEmail, projectName, duration, zone.toString());

        assertThat(actual).isSuccess();
        assertThat(actual.failureMessages()).isEmpty();
        assertThat(actual).successValues().contains(expected);
    }

    @Test
    void parseDuration() throws Exception {
        String durationString = "PT30M";
        Duration expected = Duration.ofMinutes(30);

        Duration actual = Duration.parse(durationString);

        assertThat(actual)
                .isEqualTo(expected);
    }
}