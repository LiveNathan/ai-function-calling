package dev.nathanlively.application.functions.createproject;

import dev.nathanlively.application.CreateProjectService;

import java.util.function.Function;

public class CreateProjectFunction implements Function<CreateProjectRequest, CreateProjectResponse> {
    private final CreateProjectService service;

    public CreateProjectFunction(CreateProjectService service) {
        this.service = service;
    }

    @Override
    public CreateProjectResponse apply(CreateProjectRequest request) {
        return service.withName(request);
    }
}
