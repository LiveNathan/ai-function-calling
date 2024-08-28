package dev.nathanlively.application.functions.findallprojectnames;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.domain.Project;

import java.util.function.Function;

public class FindAllProjectNamesFunction implements Function<FindAllProjectNamesRequest, FindAllProjectNamesResponse> {
    private final ProjectRepository projectRepository;

    public FindAllProjectNamesFunction(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public FindAllProjectNamesResponse apply(FindAllProjectNamesRequest findAllProjectNamesRequest) {
        return new FindAllProjectNamesResponse(projectRepository.findAll().stream().map(Project::name).toList());
    }
}
