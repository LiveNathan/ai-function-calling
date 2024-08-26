package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClockInServiceTest {

    @Test
    void clockIn_givenNullProject() throws Exception {
        ResourceRepository resourceRepository = InMemoryResourceRepository.createEmpty();
        ProjectRepository projectRepository = InMemoryProjectRepository.createEmpty();
        String resourceEmail = "nathanlively@gmail.com";
        String projectName = "Project A (12345)";
        Resource resource = new Resource(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", resourceEmail, null);
        Project project = new Project(projectName);
        resourceRepository.save(resource);
        projectRepository.save(project);
        assertThat(resourceRepository.findAll().getFirst().timeSheet().timeSheetEntries()).isEmpty();
        assertThat(projectRepository.findAll()).hasSize(1);
        ClockInService service = new ClockInService(resourceRepository, projectRepository);
        Instant clockInTime = Instant.now();
        TimesheetEntry expected = TimesheetEntry.clockIn(project, clockInTime);

        TimesheetEntry actual = service.clockIn(resourceEmail, clockInTime, projectName);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);

        List<Resource> resources = resourceRepository.findAll();
        List<Project> projects = projectRepository.findAll();
        assertThat(resources).hasSize(1);
        assertThat(projects).hasSize(1);
        assertThat(resources.getFirst().timeSheet().timeSheetEntries()).hasSize(1);
        assertThat(resources.getFirst().timeSheet().timeSheetEntries().getFirst().project()).isNotNull();
    }
}