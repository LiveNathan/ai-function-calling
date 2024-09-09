package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateTimesheetEntryServiceTest {
    private final String projectName = "Project A (12345)";
    private final Project project = Project.create(projectName);
    private final Instant clockInTime = Instant.now();
    private final String resourceEmail = "nathanlively@gmail.com";
    private UpdateTimesheetEntryService service;
    private ResourceRepository resourceRepository;
    private TimesheetEntry timesheetEntry;

    @BeforeEach
    void setUp() {
        resourceRepository = InMemoryResourceRepository.createEmpty();
        ProjectRepository projectRepository = InMemoryProjectRepository.createEmpty();
        service = new UpdateTimesheetEntryService(resourceRepository, projectRepository);
        Resource resource = Resource.create(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", resourceEmail, null);
        timesheetEntry = TimesheetEntry.clockIn(null, clockInTime);
        resource.appendTimesheetEntry(timesheetEntry);
        resourceRepository.save(resource);
        projectRepository.save(project);
    }

    @Test
    void appendProject() {
        assertThat(resourceRepository.findAll().getFirst().timesheet().timeSheetEntries().getFirst().project()).isNull();

        Result<TimesheetEntry> actual = service.updateProjectOfMostRecentTimesheetEntry(resourceEmail, projectName);

        ResultAssertions.assertThat(actual).isSuccess();
        assertThat(actual.failureMessages()).isEmpty();
        ResultAssertions.assertThat(actual).successValues().contains(timesheetEntry);

        List<TimesheetEntry> timesheetEntries = resourceRepository.findAll().getFirst().timesheet().timeSheetEntries();
        assertThat(timesheetEntries).hasSize(1);
        assertThat(timesheetEntries.getFirst().project()).isNotNull();
    }
}