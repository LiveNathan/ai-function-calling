package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.domain.Project;

import java.util.List;
import java.util.Optional;

public class UpdateProjectHoursService {
    private final ProjectRepository projectRepository;

    public UpdateProjectHoursService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Result<Project> with(String projectName, int estimatedHours) {
        List<String> validationErrors = InputValidator.validateInputs(projectName, estimatedHours);
        if (!validationErrors.isEmpty()) return Result.failure(validationErrors);

        Optional<Project> optionalProject = projectRepository.findByName(projectName);
        if (optionalProject.isEmpty()) {
            return Result.failure("Project not found with name: " + projectName);
        }

        Project project = optionalProject.get();
        project.updateEstimatedHours(estimatedHours);
        projectRepository.save(project);

        return Result.success(project);
    }
}
