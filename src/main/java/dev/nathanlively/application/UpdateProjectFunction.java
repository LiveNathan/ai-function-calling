package dev.nathanlively.application;

import dev.nathanlively.application.updateproject.UpdateProjectRequest;
import dev.nathanlively.application.updateproject.UpdateProjectResponse;

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
