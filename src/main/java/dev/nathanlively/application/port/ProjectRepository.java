package dev.nathanlively.application.port;

import dev.nathanlively.domain.Project;

import java.util.List;

public interface ProjectRepository {
    void save(Project project);
    List<Project> findAll();
}
