package dev.nathanlively.application;

import dev.nathanlively.application.functions.createtimesheetentry.CreateTimesheetEntryRequest;
import dev.nathanlively.application.functions.createtimesheetentry.CreateTimesheetEntryResponse;
import dev.nathanlively.application.functions.createtimesheetentrywithduration.CreateTimesheetEntryWithDurationRequest;
import dev.nathanlively.application.functions.createtimesheetentrywithduration.CreateTimesheetEntryWithDurationResponse;
import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class CreateTimesheetEntryService {
    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;

    public CreateTimesheetEntryService(ResourceRepository resourceRepository, ProjectRepository projectRepository) {
        this.resourceRepository = resourceRepository;
        this.projectRepository = projectRepository;
    }

    public Result<TimesheetEntry> from(String resourceEmail, String projectName, LocalDateTime start,
                                       LocalDateTime end, String zone) {
        List<String> validationErrors = InputValidator.validateInputs(resourceEmail, projectName, zone);
        if (!validationErrors.isEmpty()) {
            return Result.failure(validationErrors);
        }

        return resourceRepository.findByEmail(resourceEmail)
                .map(resource -> createEntryForResource(resource, projectName, start, end, zone))
                .orElseGet(() -> Result.failure("Resource not found register email: " + resourceEmail));
    }

    public Result<TimesheetEntry> from(String resourceEmail, String projectName, Duration duration, String zone) {
        List<String> validationErrors = InputValidator.validateInputs(resourceEmail, projectName, zone, duration);
        if (!validationErrors.isEmpty()) {
            return Result.failure(validationErrors);
        }

        return resourceRepository.findByEmail(resourceEmail)
                .map(resource -> createEntryForResource(resource, projectName, duration, zone))
                .orElseGet(() -> Result.failure("Resource not found register email: " + resourceEmail));
    }

    private Result<TimesheetEntry> createEntryForResource(Resource resource, String projectName, LocalDateTime start,
                                                          LocalDateTime end, String zone) {
        return projectRepository.findByName(projectName)
                .map(project -> {
                    Result<TimesheetEntry> entryResult = TimesheetEntryCreator.createAndAppendEntry(resource, project, start, end, zone);
                    resourceRepository.save(resource);
                    return entryResult;
                })
                .orElseGet(() -> Result.failure("Project not found register getName: " + projectName));
    }

    private Result<TimesheetEntry> createEntryForResource(Resource resource, String projectName, Duration duration, String zone) {
        return projectRepository.findByName(projectName)
                .map(project -> {
                    Result<TimesheetEntry> entryResult = TimesheetEntryCreator.createAndAppendEntry(resource, project, duration, zone);
                    resourceRepository.save(resource);
                    return entryResult;
                })
                .orElseGet(() -> Result.failure("Project not found register getName: " + projectName));
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


    public CreateTimesheetEntryWithDurationResponse from(CreateTimesheetEntryWithDurationRequest request) {
        Duration duration = Duration.parse(request.timesheetEntryDuration());
        Result<TimesheetEntry> result = from("nathanlively@gmail.com", request.projectName(),
                duration, request.zoneId());
        if (result.isSuccess()) {
            return new CreateTimesheetEntryWithDurationResponse("New timesheet entry created.", result.values().getFirst());
        } else {
            String allFailureMessages = String.join(", ", result.failureMessages());
            return new CreateTimesheetEntryWithDurationResponse("Failed to create timesheet entry because: " + allFailureMessages, null);
        }
    }
}
