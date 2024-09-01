package dev.nathanlively.application;

import dev.nathanlively.application.port.ResourceRepository;

public class CreateTimesheetEntryService {
    private final ResourceRepository resourceRepository;

    public CreateTimesheetEntryService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }
}
