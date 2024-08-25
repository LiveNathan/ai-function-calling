package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.domain.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Project findByName(String name) {
        return projects.get(name);
    }
}
