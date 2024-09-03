package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static dev.nathanlively.application.ResultAssertions.assertThat;

class ClockInServiceTest {

    private final String resourceEmail = "nathanlively@gmail.com";
    private final String projectName = "Project A (12345)";
    private final Project project = Project.create(projectName);
    private final Instant clockInTime = Instant.now();
    private ResourceRepository resourceRepository;
    private ProjectRepository projectRepository;
    private ClockInService service;

    @BeforeEach
    void setUp() {
        resourceRepository = InMemoryResourceRepository.createEmpty();
        projectRepository = InMemoryProjectRepository.createEmpty();
        service = new ClockInService(resourceRepository, projectRepository);
        Resource resource = Resource.withSystemClock(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", resourceEmail, null);
        resourceRepository.save(resource);
        projectRepository.save(project);
    }

    @Test
    void clockIn() {
        assertThat(resourceRepository.findAll().getFirst().timesheet().timeSheetEntries()).isEmpty();
        assertThat(projectRepository.findAll()).hasSize(1);
        TimesheetEntry expected = TimesheetEntry.clockIn(project, clockInTime);

        Result<TimesheetEntry> actual = service.clockIn(resourceEmail, clockInTime, projectName);

        assertThat(actual).isSuccess();
        assertThat(actual.failureMessages()).isEmpty();
        assertThat(actual).successValues().contains(expected);

        List<Resource> resources = resourceRepository.findAll();
        List<Project> projects = projectRepository.findAll();
        assertThat(resources).hasSize(1);
        assertThat(projects).hasSize(1);
        assertThat(resources.getFirst().timesheet().timeSheetEntries()).hasSize(1);
        assertThat(resources.getFirst().timesheet().timeSheetEntries().getFirst().project()).isNotNull();
    }

    @Test
    void clockIn_givenNullEmail_returnsResultWithErrorMessage() {
        Result<TimesheetEntry> actual = service.clockIn(null, clockInTime, projectName);
        assertThat(actual).isFailure().failureMessages().contains("Email must not be null or empty.");
    }

    @Test
    void clockIn_resourceNotFound_returnResultWithErrorMessage() {
        String resourceEmail = "bademail@gmail.com";
        Result<TimesheetEntry> actual = service.clockIn(resourceEmail, clockInTime, projectName);
        assertThat(actual).isFailure().failureMessages().contains("Resource not found with email: " + resourceEmail);
    }



}