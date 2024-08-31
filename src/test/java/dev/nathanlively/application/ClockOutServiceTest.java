package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static dev.nathanlively.application.ResultAssertions.assertThat;

class ClockOutServiceTest {

    private final String resourceEmail = "nathanlively@gmail.com";
    private final Instant clockInTime = Instant.now();
    private final Instant clockOutTime = clockInTime.plusSeconds(60*60);
    private ResourceRepository resourceRepository;
    private ProjectRepository projectRepository;
    private ClockOutService service;
    private TimesheetEntry timesheetEntry;
    private Project project;

    @BeforeEach
    void setUp() {
        resourceRepository = InMemoryResourceRepository.createEmpty();
        projectRepository = InMemoryProjectRepository.createEmpty();
        project = new Project("Project A (12345)");
        projectRepository.save(project);
        service = new ClockOutService(resourceRepository, projectRepository);
        Resource resource = new Resource(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", resourceEmail, null);
        timesheetEntry = TimesheetEntry.clockIn(project, clockInTime);
        resource.appendTimesheetEntry(timesheetEntry);
        resourceRepository.save(resource);
    }

    @Test
    void clockOut() {
        assertThat(resourceRepository.findAll().getFirst().timeSheet().timeSheetEntries().getFirst().workPeriod().end()).isNull();
        TimesheetEntry expected = TimesheetEntry.clockIn(project, clockInTime);
        expected.clockOut(clockOutTime);

        Result<TimesheetEntry> actual = service.clockOut(resourceEmail, clockOutTime);

        assertThat(actual).isSuccess();
        assertThat(actual.failureMessages()).isEmpty();
        assertThat(actual).successValues().contains(expected);

//        List<Resource> resources = resourceRepository.findAll();
//        List<Project> projects = projectRepository.findAll();
//        assertThat(resources).hasSize(1);
//        assertThat(projects).hasSize(1);
//        assertThat(resources.getFirst().timeSheet().timeSheetEntries()).hasSize(1);
//        assertThat(resources.getFirst().timeSheet().timeSheetEntries().getFirst().project()).isNotNull();
    }

//    @Test
//    void clockOut_givenNullEmail_returnsResultWithErrorMessage() {
//        Result<TimesheetEntry> actual = service.clockIn(null, clockInTime, projectName);
//        assertThat(actual).isFailure().failureMessages().contains("Email must not be null or empty.");
//    }

//    @Test
//    void clockOut_resourceNotFound_returnResultWithErrorMessage() {
//        String resourceEmail = "bademail@gmail.com";
//        Result<TimesheetEntry> actual = service.clockIn(resourceEmail, clockInTime, projectName);
//        assertThat(actual).isFailure().failureMessages().contains("Resource not found with email: " + resourceEmail);
//    }



}