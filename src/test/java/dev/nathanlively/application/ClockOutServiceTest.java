package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static dev.nathanlively.application.ResultAssertions.assertThat;

class ClockOutServiceTest {

    private final String resourceEmail = "nathanlively@gmail.com";
    private final Resource resource = Resource.create(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", resourceEmail, null);
    private final LocalDateTime clockInTime = LocalDateTime.of(2024, 9, 3, 9, 0);
    private final LocalDateTime clockOutTime = LocalDateTime.of(2024, 9, 3, 10, 0);
    private ResourceRepository resourceRepository;
    private ClockOutService service;
    private final ZoneId ZONE_ID = ZoneId.of("America/Chicago");

    @BeforeEach
    void setUp() {
        resourceRepository = InMemoryResourceRepository.createEmpty();
        ProjectRepository projectRepository = InMemoryProjectRepository.createEmpty();
        Project project = Project.create("Project A (12345)");
        projectRepository.save(project);
        service = new ClockOutService(resourceRepository);
        resource.timesheet().clockIn(clockInTime, ZONE_ID);
        resourceRepository.save(resource);
    }

    @Test
    void clockOut() {
        List<Resource> allResources = resourceRepository.findAll();
        Assertions.assertThat(allResources.getFirst().timesheet().timeSheetEntries()).hasSize(1);
        assertThat(allResources.getFirst().timesheet().mostRecentEntry().workPeriod().end()).isNull();
        TimesheetEntry expected = resource.timesheet().mostRecentEntry();

        Result<TimesheetEntry> actual = service.clockOut(resourceEmail, clockOutTime, ZONE_ID.toString());

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
        Result<TimesheetEntry> actual = service.clockOut(null, clockInTime, ZONE_ID.toString());
        assertThat(actual).isFailure().failureMessages().contains("Email must not be null or empty.");
    }

    @Test
    void clockOut_resourceNotFound_returnResultWithErrorMessage() {
        String resourceEmail = "bademail@gmail.com";
        Result<TimesheetEntry> actual = service.clockOut(resourceEmail, clockInTime, ZONE_ID.toString());
        assertThat(actual).isFailure().failureMessages().contains("Resource not found for email: " + resourceEmail);
    }

}