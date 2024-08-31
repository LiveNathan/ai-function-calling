package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.domain.Project;

import java.util.Optional;

public class CreateProjectService {
    private final ProjectRepository projectRepository;

    public CreateProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Result<Project> withName(String projectName) {
        if (projectName == null || projectName.trim().isEmpty()) {
            return Result.failure("Project name must not be null or empty.");
        }

        Optional<Project> similarProject = projectRepository.findByName(projectName);
        if (similarProject.isPresent()) {
            return Result.failure("There's already a project called " + similarProject.get().name() + ". Please create a more unique name.");
        }

        Project project = new Project(projectName);
        projectRepository.save(project);
        return Result.success(project);
    }
}
