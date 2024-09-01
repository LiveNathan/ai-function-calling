package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Project;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;

import java.time.LocalDateTime;

public class CreateTimesheetEntryService {
    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;

    public CreateTimesheetEntryService(ResourceRepository resourceRepository, ProjectRepository projectRepository) {
        this.resourceRepository = resourceRepository;
        this.projectRepository = projectRepository;
    }

    public Result<TimesheetEntry> from(String resourceEmail, String projectName, LocalDateTime start,
                                       LocalDateTime end, String zone) {
        if (projectName == null || projectName.trim().isEmpty()) {
            return Result.failure("Project name must not be null or empty.");
        }
        if (zone == null || zone.trim().isEmpty()) {
            return Result.failure("Zone must not be null or empty.");
        }
        if (resourceEmail == null || resourceEmail.trim().isEmpty()) {
            return Result.failure("Email must not be null or empty.");
        }
        Resource resource = resourceRepository.findByEmail(resourceEmail).orElse(null);
        if (resource == null) {
            return Result.failure("Resource not found with email: " + resourceEmail);
        }

        Project project = projectRepository.findByName(projectName).orElse(null);
        if (project == null) {
            return Result.failure("Project not found with name: " + projectName);
        }



//        ZoneId zoneId = ZoneId.of(zone);
//        ZonedDateTime zonedStart = start.atZone(zoneId);
//        ZonedDateTime zonedEnd = end.atZone(zoneId);
//        Instant startInstant = zonedStart.toInstant();
//        Instant endInstant = zonedEnd.toInstant();


        return null;
    }
}
