package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.domain.Project;

import java.util.*;

public class InMemoryProjectRepository implements ProjectRepository {

    private final Map<String, Project> projects;

    public InMemoryProjectRepository(Map<String, Project> projects) {
        this.projects = projects;
    }

    public static InMemoryProjectRepository create(Map<String, Project> projects) {
        return new InMemoryProjectRepository(projects);
    }

    public static ProjectRepository createEmpty() {
        return create(new HashMap<>());
    }

    @Override
    public void save(Project project) {
        projects.put(project.name(), project);
    }

    @Override
    public List<Project> findAll() {
        return new ArrayList<>(projects.values());
    }

    @Override
    public Optional<Project> findByName(String name) {
        List<String> allNames = findAllNames();
        return ProjectNameMatcher.from(name, allNames)
                .flatMap(cleanedName -> Optional.ofNullable(projects.get(cleanedName)));
    }

    @Override
    public List<String> findAllNames() {
        return findAll().stream().map(Project::name).toList();
    }
}
