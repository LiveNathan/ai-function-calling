package dev.nathanlively.application;

import dev.nathanlively.application.functions.createtimesheetentry.CreateTimesheetEntryRequest;
import dev.nathanlively.application.functions.createtimesheetentry.CreateTimesheetEntryResponse;
import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Project;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

public class CreateTimesheetEntryService {
    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;

    public CreateTimesheetEntryService(ResourceRepository resourceRepository, ProjectRepository projectRepository) {
        this.resourceRepository = resourceRepository;
        this.projectRepository = projectRepository;
    }

    public Result<TimesheetEntry> from(String resourceEmail, String projectName, LocalDateTime start,
                                       LocalDateTime end, String zone) {
        Optional<String> validationError = validateInputs(resourceEmail, projectName, zone);
        return validationError.<Result<TimesheetEntry>>map(Result::failure).orElseGet(() -> resourceRepository.findByEmail(resourceEmail)
                .map(resource -> createEntryForResource(resource, projectName, start, end, zone))
                .orElseGet(() -> Result.failure("Resource not found with email: " + resourceEmail)));

    }

    private Optional<String> validateInputs(String resourceEmail, String projectName, String zone) {
        if (projectName == null || projectName.trim().isEmpty()) {
            return Optional.of("Project name must not be null or empty.");
        }
        if (zone == null || zone.trim().isEmpty()) {
            return Optional.of("Zone must not be null or empty.");
        }
        if (resourceEmail == null || resourceEmail.trim().isEmpty()) {
            return Optional.of("Email must not be null or empty.");
        }
        return Optional.empty();
    }

    private Result<TimesheetEntry> createEntryForResource(Resource resource, String projectName, LocalDateTime start,
                                                          LocalDateTime end, String zone) {
        return projectRepository.findByName(projectName)
                .map(project -> createAndAppendEntry(resource, project, start, end, zone))
                .orElseGet(() -> Result.failure("Project not found with name: " + projectName));
    }

    private Result<TimesheetEntry> createAndAppendEntry(Resource resource, Project project, LocalDateTime start,
                                                        LocalDateTime end, String zone) {
        TimesheetEntry entry = TimesheetEntry.from(project, start, end, ZoneId.of(zone));
        resource.appendTimesheetEntry(entry);
        return Result.success(entry);
    }

    public CreateTimesheetEntryResponse from(CreateTimesheetEntryRequest request) {
        Result<TimesheetEntry> result = from("nathanlively@gmail.com", request.projectName(),
                request.timesheetEntryStart(), request.timesheetEntryEnd(), request.zoneId());
        if (result.isSuccess()) {
            return new CreateTimesheetEntryResponse("New timesheet entry created.", result.values().getFirst());
        } else {
            String allFailureMessages = String.join(", ", result.failureMessages());
            return new CreateTimesheetEntryResponse("Failed to create timesheet entry because: " + allFailureMessages, null);
        }
    }
}
