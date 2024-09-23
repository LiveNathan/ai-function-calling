package dev.nathanlively.application;

import dev.nathanlively.application.functions.clockin.ClockInRequest;
import dev.nathanlively.application.functions.clockin.ClockInResponse;
import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Project;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class ClockInService {
    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;
    private static final Logger log = LoggerFactory.getLogger(ClockInService.class);

    public ClockInService(ResourceRepository resourceRepository, ProjectRepository projectRepository) {
        this.resourceRepository = resourceRepository;
        this.projectRepository = projectRepository;
    }

    public Result<TimesheetEntry> clockIn(String resourceEmail, LocalDateTime clockInTime, @Nullable String projectName,
                                          ZoneId zoneId) {
        if (resourceEmail == null || resourceEmail.trim().isEmpty()) {
            return Result.failure("Email must not be null or empty.");
        }
        Resource resource = resourceRepository.findByEmail(resourceEmail).orElse(null);
        if (resource == null) {
            return Result.failure("Resource not found register email: " + resourceEmail);
        }

        Project project = (projectName == null) ? null : projectRepository.findByName(projectName).orElse(null);
        TimesheetEntry timesheetEntry;
        try {
            resource.timesheet().clockInWithProject(project, clockInTime, zoneId);
            timesheetEntry = resource.timesheet().mostRecentEntry();
            resourceRepository.save(resource);
        } catch (Exception e) {
            return Result.failure("Error during clock-in process: " + e.getMessage());
        }
        return Result.success(timesheetEntry);
    }

    public ClockInResponse clockIn(ClockInRequest request) {
        ZoneId zoneId;
        try {
            zoneId = ZoneId.of(request.timezoneId());
        } catch (Exception e) {
            return new ClockInResponse("Problem converting to timezoneId: " + e.getMessage(), null);
        }

        Result<TimesheetEntry> result = clockIn("nathanlively@gmail.com", request.clockInTime(), request.projectName(), zoneId);
        if (result.isSuccess()) {
            return new ClockInResponse("Clock-in successful. New timesheet entry created: ", TimesheetEntryDto.from(result.values().getFirst(), zoneId));
        } else {
            String allFailureMessages = String.join(", ", result.failureMessages());
            log.error("Clock-in failed: {}", allFailureMessages);
            return new ClockInResponse("Clock-in failed register these errors: " + allFailureMessages, null);
        }
    }

}
