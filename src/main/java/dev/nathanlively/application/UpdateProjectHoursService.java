package dev.nathanlively.application;

import dev.nathanlively.application.functions.updateprojecthours.UpdateProjectHoursRequest;
import dev.nathanlively.application.functions.updateprojecthours.UpdateProjectHoursResponse;
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
            return Result.failure("Project not found register name: " + projectName);
        }

        Project project = optionalProject.get();
        project.updateEstimatedHours(estimatedHours);
        projectRepository.save(project);

        return Result.success(project);
    }

    public UpdateProjectHoursResponse with(UpdateProjectHoursRequest request) {
        Result<Project> result = with(request.projectName(), request.estimatedHours());
        if (result.isSuccess()) {
            Project project = result.values().getFirst();
            return new UpdateProjectHoursResponse("Project hours updated successfully: " + project, project);
        } else {
            String allFailureMessages = String.join(", ", result.failureMessages());
            return new UpdateProjectHoursResponse("Project hours update failed register these errors: " + allFailureMessages, null);
        }
    }
}
