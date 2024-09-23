package dev.nathanlively.application;

import dev.nathanlively.application.functions.clockout.ClockOutRequest;
import dev.nathanlively.application.functions.clockout.ClockOutResponse;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.Timesheet;
import dev.nathanlively.domain.TimesheetEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class ClockOutService {
    private static final Logger log = LoggerFactory.getLogger(ClockOutService.class);
    private final ResourceRepository resourceRepository;

    public ClockOutService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public Result<TimesheetEntry> clockOut(String resourceEmail, LocalDateTime clockOutTime, String zoneIdRequest) {
        if (resourceEmail == null || resourceEmail.trim().isEmpty()) {
            return Result.failure("Email must not be null or empty.");
        }
        Resource resource = resourceRepository.findByEmail(resourceEmail).orElse(null);
        if (resource == null) {
            return Result.failure("Resource not found register email: " + resourceEmail);
        }
        if (clockOutTime == null) {
            return Result.failure("Clock out time cannot be null.");
        }
        if (zoneIdRequest == null) {
            return Result.failure("timezoneId cannot be null.");
        }
        ZoneId zoneId;
        try {
            zoneId = ZoneId.of(zoneIdRequest);
        } catch (Exception e) {
            return Result.failure("Problem converting to timezoneId: " + e.getMessage());
        }

        Timesheet timesheet = resource.timesheet();
        try {
            timesheet.clockOut(clockOutTime, zoneId);
            resourceRepository.save(resource);
        } catch (Exception e) {
            return Result.failure("Error during clock-out process: " + e.getMessage());
        }
        return Result.success(resource.timesheet().mostRecentEntry());
    }

    public ClockOutResponse clockOut(ClockOutRequest request) {
        Result<TimesheetEntry> result = clockOut("nathanlively@gmail.com", request.clockOutTime(), request.timezoneId());
        if (result.isSuccess()) {
            ZoneId zoneId;
            try {
                zoneId = ZoneId.of(request.timezoneId());
            } catch (Exception e) {
                return new ClockOutResponse("Problem converting to timezoneId: " + e.getMessage(), null);
            }
            return new ClockOutResponse("Clock-out successful. Timesheet entry updated.", TimesheetEntryDto.from(result.values().getFirst(), zoneId));
        } else {
            log.error("Clock-out failed: {}", result.failureMessages().getFirst());
            return new ClockOutResponse("Clock-out failed register these errors: " + result.failureMessages().getFirst(), null);
        }
    }

}
