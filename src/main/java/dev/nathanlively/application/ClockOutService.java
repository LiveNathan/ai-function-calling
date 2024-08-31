package dev.nathanlively.application;

import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.Timesheet;
import dev.nathanlively.domain.TimesheetEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class ClockOutService {
    private static final Logger log = LoggerFactory.getLogger(ClockOutService.class);
    private final ResourceRepository resourceRepository;

    public ClockOutService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public Result<TimesheetEntry> clockOut(String resourceEmail, Instant clockOutTime) {
        if (resourceEmail == null || resourceEmail.trim().isEmpty()) {
            return Result.failure("Email must not be null or empty.");
        }
        Resource resource = resourceRepository.findByEmail(resourceEmail).orElse(null);
        if (resource == null) {
            return Result.failure("Resource not found with email: " + resourceEmail);
        }

        Timesheet timesheet = resource.timeSheet();
        try {
            timesheet.clockOut(clockOutTime);
            resourceRepository.save(resource);
        } catch (Exception e) {
            return Result.failure("Error during clock-out process: " + e.getMessage());
        }
        return Result.success(resource.timeSheet().mostRecentEntry());
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
