package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.domain.Project;

public class CreateProjectService {
    private final ProjectRepository projectRepository;

    public CreateProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Result<Project> withName(String projectName) {
        if (projectName == null || projectName.trim().isEmpty()) {
            return Result.failure("Project name must not be null or empty.");
        }
        Project project = new Project(projectName);
        projectRepository.save(project);
        return Result.success(project);
    }
}
