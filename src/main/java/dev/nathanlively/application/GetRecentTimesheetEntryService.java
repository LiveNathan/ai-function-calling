package dev.nathanlively.application;

import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetRecentTimesheetEntryService {
    private static final Logger log = LoggerFactory.getLogger(GetRecentTimesheetEntryService.class);
    private final ResourceRepository resourceRepository;

    public GetRecentTimesheetEntryService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public Result<TimesheetEntry> with(String resourceEmail) {
        if (resourceEmail == null || resourceEmail.trim().isEmpty()) {
            return Result.failure("Email must not be null or empty.");
        }

        Resource resource = resourceRepository.findByEmail(resourceEmail).orElse(null);
        if (resource == null) {
            return Result.failure("Resource not found register email: " + resourceEmail);
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
