package dev.nathanlively.application.functions.createtimesheetentrywithduration;

import dev.nathanlively.application.CreateTimesheetEntryService;

import java.util.function.Function;

public class CreateTimesheetEntryWithDurationFunction implements Function<CreateTimesheetEntryWithDurationRequest, CreateTimesheetEntryWithDurationResponse> {
    private final CreateTimesheetEntryService createTimesheetEntryService;

    public CreateTimesheetEntryWithDurationFunction(CreateTimesheetEntryService createTimesheetEntryService) {
        this.createTimesheetEntryService = createTimesheetEntryService;
    }

    @Override
    public CreateTimesheetEntryWithDurationResponse apply(CreateTimesheetEntryWithDurationRequest createTimesheetEntryWithDurationRequest) {
        return createTimesheetEntryService.from(createTimesheetEntryWithDurationRequest);
    }
}
