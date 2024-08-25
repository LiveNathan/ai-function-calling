package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;

import java.time.Instant;
import java.util.Objects;

public class ClockInService {
    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;

    public ClockInService(ResourceRepository resourceRepository, ProjectRepository projectRepository) {
        this.resourceRepository = resourceRepository;
        this.projectRepository = projectRepository;
    }

    public TimesheetEntry clockIn(String resourceEmail, Instant clockInTime, String projectName) {
        Objects.requireNonNull(resourceEmail, "Email must not be null");  // todo: return some message to user instead?
        Resource resource = resourceRepository.findByEmail(resourceEmail)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found for: " + resourceEmail));
        // get project
        TimesheetEntry timesheetEntry = TimesheetEntry.clockIn(clockInTime);
        resource.appendTimesheetEntry(timesheetEntry);
        resourceRepository.save(resource);
        return timesheetEntry;
    }

}
