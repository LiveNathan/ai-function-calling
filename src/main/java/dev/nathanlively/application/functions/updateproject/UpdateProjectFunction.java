package dev.nathanlively.application.functions.updateproject;

import dev.nathanlively.application.UpdateTimesheetEntryService;

import java.util.function.Function;

public class UpdateProjectFunction implements Function<UpdateProjectRequest, UpdateProjectResponse> {
    private final UpdateTimesheetEntryService updateTimesheetEntryService;

    public UpdateProjectFunction(UpdateTimesheetEntryService updateTimesheetEntryService) {
        this.updateTimesheetEntryService = updateTimesheetEntryService;
    }

    @Override
    public UpdateProjectResponse apply(UpdateProjectRequest updateProjectRequest) {
        return updateTimesheetEntryService.updateProjectOfMostRecentTimesheetEntry(updateProjectRequest);
    }
}
