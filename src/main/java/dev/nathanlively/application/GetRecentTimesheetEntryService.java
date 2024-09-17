package dev.nathanlively.application;

import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;
import dev.nathanlively.security.AuthenticatedUser;
import dev.nathanlively.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class GetRecentTimesheetEntryService {
    private static final Logger log = LoggerFactory.getLogger(GetRecentTimesheetEntryService.class);
    private final ResourceRepository resourceRepository;
    private final AuthenticatedUser authenticatedUser;

    public GetRecentTimesheetEntryService(ResourceRepository resourceRepository, AuthenticatedUser authenticatedUser) {
        this.resourceRepository = resourceRepository;
        this.authenticatedUser = authenticatedUser;
    }

    public Result<TimesheetEntry> with() {
        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isEmpty()) {
            log.warn("User is not authenticated.");
            return Result.failure("User is not authenticated.");
        }

        String username = maybeUser.get().getUsername();
        Resource resource = resourceRepository.findByEmail(username).orElse(null);
        if (resource == null) {
            return Result.failure("Resource not found register email: " + username);
        }

        TimesheetEntry mostRecentEntry = null;
        try {
            mostRecentEntry = resource.timesheet().mostRecentEntry();
        } catch (Exception e) {
            return Result.failure("Error fetching timesheet: " + e.getMessage());
        }
        return Result.success(mostRecentEntry);
    }

//    public UpdateProjectResponse updateProjectOfMostRecentTimesheetEntry(UpdateProjectRequest request) {
//        Result<TimesheetEntry> result = updateProjectOfMostRecentTimesheetEntry("nathanlively@gmail.com", request.projectName());
//        if (result.isSuccess()) {
//            TimesheetEntry timesheetEntry = result.values().getFirst();
//            return new UpdateProjectResponse("Timesheet update successful: " + timesheetEntry.toString(), timesheetEntry);
//        } else {
//            log.error("Timesheet update failed: {}", result.failureMessages().getFirst());
//            return new UpdateProjectResponse("Timesheet update register these errors: " + result.failureMessages().getFirst(), null);
//        }
//    }
}
