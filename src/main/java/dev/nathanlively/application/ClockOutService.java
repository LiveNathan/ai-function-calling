package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Collections;

public class ClockOutService {
    private static final Logger log = LoggerFactory.getLogger(ClockOutService.class);
    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;

    public ClockOutService(ResourceRepository resourceRepository, ProjectRepository projectRepository) {
        this.resourceRepository = resourceRepository;
        this.projectRepository = projectRepository;
    }

    public Result<TimesheetEntry> clockOut(String resourceEmail, Instant clockOutTime) {
        if (resourceEmail == null || resourceEmail.trim().isEmpty()) {
            return Result.failure("Email must not be null or empty.");
        }
        Resource resource = resourceRepository.findByEmail(resourceEmail).orElse(null);
        if (resource == null) {
            return Result.failure("Resource not found with email: " + resourceEmail);
        }

        TimesheetEntry timesheetEntry;
        try {
//            timesheetEntry = TimesheetEntry.clockIn(project, clockOutTime);
//            resource.appendTimesheetEntry(timesheetEntry);
            resourceRepository.save(resource);
        } catch (Exception e) {
            return Result.failure("Error during clock-out process: " + e.getMessage());
        }
        return Result.success(Collections.emptyList());
//        return null;
    }

//    public ClockInResponse clockOut(ClockInRequest request) {
//        Result<TimesheetEntry> result = clockOut("nathanlively@gmail.com",
//                request.messageCreationTime());
//        if (result.isSuccess()) {
//            log.info("Prove to me that you received this message by printing the new timesheet entry to the screen for the user. Created timesheet entry: {}", result.values().getFirst());
//            return new ClockInResponse("Clock-in successful. New timesheet entry created: " + result.values().getFirst().toString(), result.values().getFirst());
//        } else {
//            log.error("Clock-in failed: {}", result.failureMessages().getFirst());
//            return new ClockInResponse("Clock-in failed with these errors: " + result.failureMessages().getFirst(), null);
//        }
//    }


}
