package dev.nathanlively.application;

import dev.nathanlively.application.functions.updateproject.UpdateProjectRequest;
import dev.nathanlively.application.functions.updateproject.UpdateProjectResponse;
import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Project;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateTimesheetEntryService {
    private static final Logger log = LoggerFactory.getLogger(UpdateTimesheetEntryService.class);
    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;

    public UpdateTimesheetEntryService(ResourceRepository resourceRepository, ProjectRepository projectRepository) {
        this.resourceRepository = resourceRepository;
        this.projectRepository = projectRepository;
    }

    public Result<TimesheetEntry> updateProjectOfMostRecentTimesheetEntry(String resourceEmail, String projectName) {
        if (resourceEmail == null || resourceEmail.trim().isEmpty()) {
            return Result.failure("Email must not be null or empty.");
        }
        if (projectName == null || projectName.trim().isEmpty()) {
            return Result.failure("Project name must not be null or empty.");
        }

        Resource resource = resourceRepository.findByEmail(resourceEmail).orElse(null);
        if (resource == null) {
            return Result.failure("Resource not found register email: " + resourceEmail);
        }

        Project project = projectRepository.findByName(projectName).orElse(null);
        if (project == null) {
            return Result.failure("Project not found register name: " + projectName);
        }

        TimesheetEntry mostRecentEntry = resource.timesheet().mostRecentEntry();
        mostRecentEntry.setProject(project);
        resourceRepository.save(resource);
        return Result.success(mostRecentEntry);
    }

    public UpdateProjectResponse updateProjectOfMostRecentTimesheetEntry(UpdateProjectRequest request) {
        Result<TimesheetEntry> result = updateProjectOfMostRecentTimesheetEntry("nathanlively@gmail.com", request.projectName());
        if (result.isSuccess()) {
            TimesheetEntry timesheetEntry = result.values().getFirst();
            return new UpdateProjectResponse("Timesheet update successful: " + timesheetEntry.toString(), timesheetEntry);
        } else {
            log.error("Timesheet update failed: {}", result.failureMessages().getFirst());
            return new UpdateProjectResponse("Timesheet update register these errors: " + result.failureMessages().getFirst(), null);
        }
    }
}
