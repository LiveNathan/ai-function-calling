package dev.nathanlively.application;

import dev.nathanlively.application.functions.clockin.ClockInRequest;
import dev.nathanlively.application.functions.clockin.ClockInResponse;
import dev.nathanlively.application.functions.updateproject.UpdateProjectRequest;
import dev.nathanlively.application.functions.updateproject.UpdateProjectResponse;
import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Project;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.Objects;

@Validated
public class ClockInService {
    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;
    private static final Logger log = LoggerFactory.getLogger(ClockInService.class);

    public ClockInService(ResourceRepository resourceRepository, ProjectRepository projectRepository) {
        this.resourceRepository = resourceRepository;
        this.projectRepository = projectRepository;
    }

    public Result<TimesheetEntry> clockIn(@NotBlank String resourceEmail, @NotNull Instant clockInTime,
                                          @Nullable String projectName) {
        Objects.requireNonNull(resourceEmail, "Email must not be null");  // todo: return some message to user instead?
        Resource resource = resourceRepository.findByEmail(resourceEmail)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found for: " + resourceEmail));
        Project project = (projectName == null) ? null : projectRepository.findByName(projectName).orElse(null);
        TimesheetEntry timesheetEntry = TimesheetEntry.clockIn(project, clockInTime);
        resource.appendTimesheetEntry(timesheetEntry);
        resourceRepository.save(resource);
        return Result.success(timesheetEntry);
    }

    public ClockInResponse clockIn(ClockInRequest request) {
        Result<TimesheetEntry> result = clockIn("nathanlively@gmail.com",
                request.messageCreationTime(), request.projectName());
        if (result.isSuccess()) {
            log.info("Created timesheet entry: {}", result.values().getFirst());
            return new ClockInResponse("Clock-in successful. New timesheet entry created: " + result.values().getFirst().toString(), result.values().getFirst());
        } else {
            log.error("Clock-in failed: {}", result.failureMessages().getFirst());
            return new ClockInResponse("Clock-in failed: " + result.failureMessages().getFirst(), null);
        }
    }

    public TimesheetEntry updateProjectOfMostRecentTimesheetEntry(String resourceEmail, String projectName) {
        Objects.requireNonNull(resourceEmail, "Email must not be null");
        Objects.requireNonNull(projectName, "Project name must not be null");

        Resource resource = resourceRepository.findByEmail(resourceEmail)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found for: " + resourceEmail));
        Project project = projectRepository.findByName(projectName)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectName));

        TimesheetEntry mostRecentEntry = resource.timeSheet().mostRecentEntry();
        mostRecentEntry.setProject(project);
        resourceRepository.save(resource);
        return mostRecentEntry;
    }

    public UpdateProjectResponse updateProjectOfMostRecentTimesheetEntry(UpdateProjectRequest request) {
        TimesheetEntry timesheetEntry = updateProjectOfMostRecentTimesheetEntry("nathanlively@gmail.com", request.projectName());
        return new UpdateProjectResponse("Timesheet update successful.", timesheetEntry);
    }
}
