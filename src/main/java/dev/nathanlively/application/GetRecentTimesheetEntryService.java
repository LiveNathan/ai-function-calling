package dev.nathanlively.application;

import dev.nathanlively.application.functions.getrecenttimesheetentry.GetRecentTimesheetEntryRequest;
import dev.nathanlively.application.functions.getrecenttimesheetentry.GetRecentTimesheetEntryResponse;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class GetRecentTimesheetEntryService {
    private static final Logger log = LoggerFactory.getLogger(GetRecentTimesheetEntryService.class);
    private final ResourceRepository resourceRepository;

    public GetRecentTimesheetEntryService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public Result<TimesheetEntry> with(String username) {
        if (username == null || username.trim().isEmpty()) {
            return Result.failure("Email must not be null or empty.");
        }
        Resource resource = resourceRepository.findByEmail(username).orElse(null);
        if (resource == null) {
            return Result.failure("Resource not found register email: " + username);
        }

        TimesheetEntry mostRecentEntry;
        try {
            mostRecentEntry = resource.timesheet().mostRecentEntry();
        } catch (Exception e) {
            return Result.failure("Error fetching timesheet: " + e.getMessage());
        }
        return Result.success(mostRecentEntry);
    }

    public GetRecentTimesheetEntryResponse forAi(GetRecentTimesheetEntryRequest request) {
        Result<TimesheetEntry> result = with(request.username());
        if (result.isSuccess()) {
            TimesheetEntry timesheetEntry = result.values().getFirst();
            return new GetRecentTimesheetEntryResponse("Most recent timesheet entry found.", timesheetEntry);
        } else {
            log.error("Timesheet update failed: {}", result.failureMessages().getFirst());
            return new GetRecentTimesheetEntryResponse("Fetching timesheet entry produced these errors: " + result.failureMessages().getFirst(), null);
        }
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.debug("No authentication object found in security context.");
            return null;
        }

        log.debug("Authentication object found: {}", authentication);

        if (!authentication.isAuthenticated()) {
            log.debug("Authentication object is not authenticated.");
            return null;
        }

        Object principal = authentication.getPrincipal();
        log.debug("Authentication principal: {}", principal);

        if (principal instanceof String) {
            log.debug("Principal is a string, likely 'anonymousUser'.");
            return null;
        }

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            log.debug("Authenticated username: {}", username);
            return username;
        }

        log.debug("Principal is not an instance of UserDetails.");
        return null;
    }
}
