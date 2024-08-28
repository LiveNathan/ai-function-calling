package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static dev.nathanlively.application.ResultAssertions.assertThat;

class ClockInServiceTest {

    private final String resourceEmail = "nathanlively@gmail.com";
    private final String projectName = "Project A (12345)";
    private final Project project = new Project(projectName);
    private final Instant clockInTime = Instant.now();
    private ResourceRepository resourceRepository;
    private ProjectRepository projectRepository;
    private ClockInService service;

    @BeforeEach
    void setUp() {
        resourceRepository = InMemoryResourceRepository.createEmpty();
        projectRepository = InMemoryProjectRepository.createEmpty();
        service = new ClockInService(resourceRepository, projectRepository);
        Resource resource = new Resource(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", resourceEmail, null);
        resourceRepository.save(resource);
        projectRepository.save(project);
    }

    @Test
    void clockIn_givenNullProject() {
        assertThat(resourceRepository.findAll().getFirst().timeSheet().timeSheetEntries()).isEmpty();
        assertThat(projectRepository.findAll()).hasSize(1);
        TimesheetEntry expected = TimesheetEntry.clockIn(project, clockInTime);

        Result<TimesheetEntry> actual = service.clockIn(resourceEmail, clockInTime, projectName);

        assertThat(actual).isSuccess();
        assertThat(actual.failureMessages()).isEmpty();
        assertThat(actual)
                .successValues()
                .contains(expected);

        List<Resource> resources = resourceRepository.findAll();
        List<Project> projects = projectRepository.findAll();
        assertThat(resources).hasSize(1);
        assertThat(projects).hasSize(1);
        assertThat(resources.getFirst().timeSheet().timeSheetEntries()).hasSize(1);
        assertThat(resources.getFirst().timeSheet().timeSheetEntries().getFirst().project()).isNotNull();
    }

    @Test
    @Disabled("until refactor to Result")
    void clockIn_givenNullEmail_returnsResultWithErrorMessage() throws Exception {

//        assertThat(actual)
//                .isEqualTo(expected);
    }

    @Test
    void appendProject() {
        TimesheetEntry expected = TimesheetEntry.clockIn(project, clockInTime);
        service.clockIn(resourceEmail, clockInTime, null);
        assertThat(resourceRepository.findAll().getFirst().timeSheet().timeSheetEntries().getFirst().project()).isNull();

        TimesheetEntry actual = service.updateProjectOfMostRecentTimesheetEntry(resourceEmail, projectName);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);

        List<TimesheetEntry> timesheetEntries = resourceRepository.findAll().getFirst().timeSheet().timeSheetEntries();
        assertThat(timesheetEntries).hasSize(1);
        assertThat(timesheetEntries.getFirst().project()).isNotNull();
    }

}