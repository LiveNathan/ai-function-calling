package dev.nathanlively.application.functions.updateprojecthours;

import dev.nathanlively.application.UpdateProjectHoursService;

import java.util.function.Function;

public class UpdateProjectHoursFunction implements Function<UpdateProjectHoursRequest, UpdateProjectHoursResponse> {
    private final UpdateProjectHoursService service;

    public UpdateProjectHoursFunction(UpdateProjectHoursService service) {
        this.service = service;
    }

    @Override
    public UpdateProjectHoursResponse apply(UpdateProjectHoursRequest request) {
        return service.with(request);
    }
}
