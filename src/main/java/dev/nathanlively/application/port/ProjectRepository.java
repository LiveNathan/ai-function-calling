package dev.nathanlively.application.port;

import dev.nathanlively.domain.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    void save(Project project);
    List<Project> findAll();

    Optional<Project> findByName(String projectName);

    List<String> findAllNames();
}
