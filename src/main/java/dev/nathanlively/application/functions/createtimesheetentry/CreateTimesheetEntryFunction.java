package dev.nathanlively.application.functions.createtimesheetentry;

import dev.nathanlively.application.CreateTimesheetEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class CreateTimesheetEntryFunction implements Function<CreateTimesheetEntryRequest, CreateTimesheetEntryResponse> {
    private final CreateTimesheetEntryService createTimesheetEntryService;
    private static final Logger log = LoggerFactory.getLogger(CreateTimesheetEntryFunction.class);

    public CreateTimesheetEntryFunction(CreateTimesheetEntryService createTimesheetEntryService) {
        this.createTimesheetEntryService = createTimesheetEntryService;
    }

    @Override
    public CreateTimesheetEntryResponse apply(CreateTimesheetEntryRequest createTimesheetEntryRequest) {
        CreateTimesheetEntryResponse response = createTimesheetEntryService.from(createTimesheetEntryRequest);
        log.info("CreateTimesheetEntryResponse: " + response);
        return response;
    }
}
