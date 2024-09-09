package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static dev.nathanlively.application.ResultAssertions.assertThat;

class ClockOutServiceTest {

    private final String resourceEmail = "nathanlively@gmail.com";
    private final Instant clockInTime = Instant.now().minusSeconds(60*60);
    private final Instant clockOutTime = Instant.now();
    private ResourceRepository resourceRepository;
    private ProjectRepository projectRepository;
    private ClockOutService service;
    private TimesheetEntry timesheetEntry;
    private Project project;

    @BeforeEach
    void setUp() {
        resourceRepository = InMemoryResourceRepository.createEmpty();
        projectRepository = InMemoryProjectRepository.createEmpty();
        project = Project.create("Project A (12345)");
        projectRepository.save(project);
        service = new ClockOutService(resourceRepository);
        Resource resource = Resource.create(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", resourceEmail, null);
        timesheetEntry = TimesheetEntry.clockIn(project, clockInTime);
        resource.appendTimesheetEntry(timesheetEntry);
        resourceRepository.save(resource);
    }

    @Test
    void clockOut() {
        List<Resource> allResources = resourceRepository.findAll();
        Assertions.assertThat(allResources.getFirst().timesheet().timeSheetEntries()).hasSize(1);
        assertThat(allResources.getFirst().timesheet().mostRecentEntry().workPeriod().end()).isNull();
        TimesheetEntry expected = TimesheetEntry.clockIn(project, clockInTime);
        expected.clockOut(clockOutTime);

        Result<TimesheetEntry> actual = service.clockOut(resourceEmail, clockOutTime);

        assertThat(actual).isSuccess();
        assertThat(actual.failureMessages()).isEmpty();
        assertThat(actual).successValues().contains(expected);

        List<Resource> resources = resourceRepository.findAll();
        assertThat(resources).hasSize(1);
        assertThat(resources.getFirst().timesheet().timeSheetEntries()).hasSize(1);
        assertThat(resources.getFirst().timesheet().mostRecentEntry().workPeriod().end()).isNotNull();
    }

    @Test
    void clockOut_givenNullEmail_returnsResultWithErrorMessage() {
        Result<TimesheetEntry> actual = service.clockOut(null, clockInTime);
        assertThat(actual).isFailure().failureMessages().contains("Email must not be null or empty.");
    }

    @Test
    void clockOut_resourceNotFound_returnResultWithErrorMessage() {
        String resourceEmail = "bademail@gmail.com";
        Result<TimesheetEntry> actual = service.clockOut(resourceEmail, clockInTime);
        assertThat(actual).isFailure().failureMessages().contains("Resource not found register email: " + resourceEmail);
    }

}