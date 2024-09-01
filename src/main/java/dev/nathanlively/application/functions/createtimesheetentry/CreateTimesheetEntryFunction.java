package dev.nathanlively.application.functions.createtimesheetentry;

import dev.nathanlively.application.CreateTimesheetEntryService;

import java.util.function.Function;

public class CreateTimesheetEntryFunction implements Function<CreateTimesheetEntryRequest, CreateTimesheetEntryResponse> {
    private final CreateTimesheetEntryService createTimesheetEntryService;

    public CreateTimesheetEntryFunction(CreateTimesheetEntryService createTimesheetEntryService) {
        this.createTimesheetEntryService = createTimesheetEntryService;
    }

    @Override
    public CreateTimesheetEntryResponse apply(CreateTimesheetEntryRequest createTimesheetEntryRequest) {
        return createTimesheetEntryService.from(createTimesheetEntryRequest);
    }
}
