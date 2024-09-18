package dev.nathanlively.application.functions.getrecenttimesheetentry;

import dev.nathanlively.application.GetRecentTimesheetEntryService;

import java.util.function.Function;

public class GetRecentTimesheetEntryFunction implements Function<GetRecentTimesheetEntryRequest, GetRecentTimesheetEntryResponse> {
    private final GetRecentTimesheetEntryService service;

    public GetRecentTimesheetEntryFunction(GetRecentTimesheetEntryService service) {
        this.service = service;
    }

    @Override
    public GetRecentTimesheetEntryResponse apply(GetRecentTimesheetEntryRequest request) {
        GetRecentTimesheetEntryResponse response = service.forAi(request);
        return response;
    }
}
