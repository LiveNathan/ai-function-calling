package dev.nathanlively.application.functions.createtimesheetentrywithduration;

import dev.nathanlively.application.CreateTimesheetEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class CreateTimesheetEntryWithDurationFunction implements Function<CreateTimesheetEntryWithDurationRequest, CreateTimesheetEntryWithDurationResponse> {
    private static final Logger log = LoggerFactory.getLogger(CreateTimesheetEntryWithDurationFunction.class);
    private final CreateTimesheetEntryService createTimesheetEntryService;

    public CreateTimesheetEntryWithDurationFunction(CreateTimesheetEntryService createTimesheetEntryService) {
        this.createTimesheetEntryService = createTimesheetEntryService;
    }

    @Override
    public CreateTimesheetEntryWithDurationResponse apply(CreateTimesheetEntryWithDurationRequest createTimesheetEntryWithDurationRequest) {
        CreateTimesheetEntryWithDurationResponse response = createTimesheetEntryService.from(createTimesheetEntryWithDurationRequest);
        return response;
    }
}
