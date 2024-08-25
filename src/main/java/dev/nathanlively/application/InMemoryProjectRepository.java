package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.domain.Project;

import java.util.ArrayList;
import java.util.List;

public class InMemoryProjectRepository implements ProjectRepository {
    private final List<Project> projects;

    public InMemoryProjectRepository(List<Project> projects) {
        this.projects = projects;
    }

    public static InMemoryProjectRepository create(List<Project> projects) {
        return new InMemoryProjectRepository(projects);
    }

    public static ProjectRepository createEmpty() {
        return create(new ArrayList<>());
    }

    @Override
    public void save(Project project) {
        projects.add(project);
    }

    @Override
    public List<Project> findAll() {
        return projects;
    }
}
