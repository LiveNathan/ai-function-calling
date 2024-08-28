package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.domain.Project;

import java.util.List;
import java.util.function.Function;

public class FindAllProjectNamesFunction implements Function<Void, List<String>> {
    private final ProjectRepository projectRepository;

    public FindAllProjectNamesFunction(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<String> apply(Void unused) {
        return projectRepository.findAll().stream().map(Project::name).toList();
    }
}
