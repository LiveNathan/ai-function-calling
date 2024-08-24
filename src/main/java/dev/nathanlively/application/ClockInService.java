package dev.nathanlively.application;

import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.TimesheetEntry;

import java.time.Instant;

public class ClockInService {
    private final ResourceRepository resourceRepository;

    public ClockInService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public TimesheetEntry clockIn(String resourceEmail, Instant clockInTime, String projectName) {
    return     new TimesheetEntry(clockInTime, null, null, null);
    }


//    public void clockIn(String resourceName, ResourceType resourceType, LocalDateTime clockInTime) {
//        // Retrieve or create a new resource
//        Resource resource = resourceRepository.findByName(resourceName)
//                .orElse(new Resource(resourceName, resourceType));
//
//        // Create a new timesheet entry and associate it with the resource
//        TimesheetEntry entry = new TimesheetEntry(clockInTime);
//        resource.addTimesheetEntry(entry);
//
//        // Save the resource (and the timesheet entry)
//        resourceRepository.save(resource);
//    }
}
