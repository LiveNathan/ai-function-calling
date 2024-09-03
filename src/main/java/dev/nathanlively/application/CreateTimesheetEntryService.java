package dev.nathanlively.application;

import dev.nathanlively.application.functions.createtimesheetentry.CreateTimesheetEntryRequest;
import dev.nathanlively.application.functions.createtimesheetentry.CreateTimesheetEntryResponse;
import dev.nathanlively.application.functions.createtimesheetentrywithduration.CreateTimesheetEntryWithDurationRequest;
import dev.nathanlively.application.functions.createtimesheetentrywithduration.CreateTimesheetEntryWithDurationResponse;
import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Project;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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
        List<String> validationErrors = validateInputs(resourceEmail, projectName, zone);
        if (!validationErrors.isEmpty()) {
            return Result.failure(validationErrors);
        }

        return resourceRepository.findByEmail(resourceEmail)
                .map(resource -> createEntryForResource(resource, projectName, start, end, zone))
                .orElseGet(() -> Result.failure("Resource not found with email: " + resourceEmail));
    }

    public Result<TimesheetEntry> from(String resourceEmail, String projectName, Duration duration, String zone) {
        List<String> validationErrors = validateInputs(resourceEmail, projectName, zone, duration);
        if (!validationErrors.isEmpty()) {
            return Result.failure(validationErrors);
        }

        return resourceRepository.findByEmail(resourceEmail)
                .map(resource -> createEntryForResource(resource, projectName, duration, zone))
                .orElseGet(() -> Result.failure("Resource not found with email: " + resourceEmail));
    }

    private List<String> validateInputs(String resourceEmail, String projectName, String zone) {
        List<String> errors = new ArrayList<>();
        if (projectName == null || projectName.trim().isEmpty()) {
            errors.add("Project name must not be null or empty.");
        }
        if (zone == null || zone.trim().isEmpty()) {
            errors.add("Zone must not be null or empty.");
        }
        if (resourceEmail == null || resourceEmail.trim().isEmpty()) {
            errors.add("Email must not be null or empty.");
        }
        return errors;
    }

    private List<String> validateInputs(String resourceEmail, String projectName, String zone, Duration duration) {
        List<String> errors = validateInputs(resourceEmail, projectName, zone);
        if (duration == null || duration.isZero()) {
            errors.add("Duration must not be null or zero.");
        }
        return errors;
    }

    private Result<TimesheetEntry> createEntryForResource(Resource resource, String projectName, LocalDateTime start,
                                                          LocalDateTime end, String zone) {
        return projectRepository.findByName(projectName)
                .map(project -> createAndAppendEntry(resource, project, start, end, zone))
                .orElseGet(() -> Result.failure("Project not found with name: " + projectName));
    }

    private Result<TimesheetEntry> createEntryForResource(Resource resource, String projectName,
                                                          Duration duration, String zone) {
        return projectRepository.findByName(projectName)
                .map(project -> createAndAppendEntry(resource, project, duration, zone))
                .orElseGet(() -> Result.failure("Project not found with name: " + projectName));
    }

    private Result<TimesheetEntry> createAndAppendEntry(Resource resource, Project project, LocalDateTime start,
                                                        LocalDateTime end, String zone) {
        TimesheetEntry entry = TimesheetEntry.from(project, start, end, ZoneId.of(zone));
        resource.appendTimesheetEntry(entry);
        resourceRepository.save(resource);
        return Result.success(entry);
    }

    private Result<TimesheetEntry> createAndAppendEntry(Resource resource, Project project,
                                                        Duration duration, String zone) {
        resource.appendTimesheetEntry(project, duration, ZoneId.of(zone));
        resourceRepository.save(resource);
        return Result.success(resource.timesheet().mostRecentEntry());
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
