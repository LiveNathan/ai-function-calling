package dev.nathanlively.application.functions.updateproject;

import dev.nathanlively.application.ClockInService;

import java.util.function.Function;

public class UpdateProjectFunction implements Function<UpdateProjectRequest, UpdateProjectResponse> {
    private final ClockInService clockInService;

    public UpdateProjectFunction(ClockInService clockInService) {
        this.clockInService = clockInService;
    }

    @Override
    public UpdateProjectResponse apply(UpdateProjectRequest updateProjectRequest) {
        return clockInService.updateProjectOfMostRecentTimesheetEntry(updateProjectRequest);
    }
}
