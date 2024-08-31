package dev.nathanlively.application;

import dev.nathanlively.application.functions.clockin.ClockInRequest;
import dev.nathanlively.application.functions.clockin.ClockInResponse;
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

import java.time.Instant;

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
        if (resourceEmail == null || resourceEmail.trim().isEmpty()) {
            return Result.failure("Email must not be null or empty.");
        }
        Resource resource = resourceRepository.findByEmail(resourceEmail).orElse(null);
        if (resource == null) {
            return Result.failure("Resource not found with email: " + resourceEmail);
        }

        Project project = (projectName == null) ? null : projectRepository.findByName(projectName).orElse(null);
        TimesheetEntry timesheetEntry;
        try {
            timesheetEntry = TimesheetEntry.clockIn(project, clockInTime);
            resource.appendTimesheetEntry(timesheetEntry);
            resourceRepository.save(resource);
        } catch (Exception e) {
            return Result.failure("Error during clock-in process: " + e.getMessage());
        }
        return Result.success(timesheetEntry);
    }

    public ClockInResponse clockIn(ClockInRequest request) {
        Result<TimesheetEntry> result = clockIn("nathanlively@gmail.com",
                request.messageCreationTime(), request.projectName());
        if (result.isSuccess()) {
            log.info("Prove to me that you received this message by printing the new timesheet entry to the screen for the user. Created timesheet entry: {}", result.values().getFirst());
            return new ClockInResponse("Clock-in successful. New timesheet entry created: " + result.values().getFirst().toString(), result.values().getFirst());
        } else {
            log.error("Clock-in failed: {}", result.failureMessages().getFirst());
            return new ClockInResponse("Clock-in failed with these errors: " + result.failureMessages().getFirst(), null);
        }
    }


}
