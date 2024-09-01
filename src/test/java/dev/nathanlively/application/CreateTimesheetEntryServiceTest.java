package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Project;
import dev.nathanlively.domain.TimesheetEntry;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static dev.nathanlively.application.ResultAssertions.assertThat;

class CreateTimesheetEntryServiceTest {
    @Test
    @Disabled("until domain")
    void from() throws Exception {
        ResourceRepository resourceRepository = InMemoryResourceRepository.createEmpty();
        ProjectRepository projectRepository = InMemoryProjectRepository.createEmpty();
        CreateTimesheetEntryService service = new CreateTimesheetEntryService(resourceRepository, projectRepository);
        String resourceEmail = "nathanlively@gmail.com";
        String projectName = "Project A";
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 8, 0);
        LocalDateTime end = LocalDateTime.of(2024, 3, 15, 9, 0);
        ZoneId zone = ZoneId.of("America/Chicago");
        Instant expectedStartInstant = start.atZone(zone).toInstant();
        Instant expectedEndInstant = end.atZone(zone).toInstant();
        TimesheetEntry expected = TimesheetEntry.clockIn(new Project(projectName), expectedStartInstant);
        expected.clockOut(expectedEndInstant);

        Result<TimesheetEntry> actual = service.from(resourceEmail, projectName, start, end, zone.toString());

        assertThat(actual)
               .isSuccess();
    }
}