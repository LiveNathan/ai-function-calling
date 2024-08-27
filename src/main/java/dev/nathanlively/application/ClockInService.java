package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Project;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Objects;

public class ClockInService {
    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;
    private static final Logger log = LoggerFactory.getLogger(ClockInService.class);

    public ClockInService(ResourceRepository resourceRepository, ProjectRepository projectRepository) {
        this.resourceRepository = resourceRepository;
        this.projectRepository = projectRepository;
    }

    public TimesheetEntry clockIn(String resourceEmail, Instant clockInTime, String projectName) {
        Objects.requireNonNull(resourceEmail, "Email must not be null");  // todo: return some message to user instead?
        Resource resource = resourceRepository.findByEmail(resourceEmail)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found for: " + resourceEmail));
        Project project = projectRepository.findByName(projectName).orElse(null);
        TimesheetEntry timesheetEntry = TimesheetEntry.clockIn(project, clockInTime);
        resource.appendTimesheetEntry(timesheetEntry);
        resourceRepository.save(resource);
        return timesheetEntry;
    }

    public ClockInResponse clockIn(ClockInRequest request) {
        TimesheetEntry timesheetEntry = clockIn("nathanlively@gmail.com", request.clockInTime(), request.projectName());
        log.info("Created timesheet entry: {}", timesheetEntry);
        return new ClockInResponse("Clock-in successful", timesheetEntry);
    }
}
