package dev.nathanlively.application.functions.getrecenttimesheetentry;

import dev.nathanlively.application.GetRecentTimesheetEntryService;

import java.util.function.Function;

public class GetRecentTimesheetEntryFunction implements Function<Void, GetRecentTimesheetEntryResponse> {
    private final GetRecentTimesheetEntryService service;

    public GetRecentTimesheetEntryFunction(GetRecentTimesheetEntryService service) {
        this.service = service;
    }

    @Override
    public GetRecentTimesheetEntryResponse apply(Void unused) {
        return service.forAi();
    }
}
