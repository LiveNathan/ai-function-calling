package dev.nathanlively.application;

import dev.nathanlively.application.functions.createproject.CreateProjectRequest;
import dev.nathanlively.application.functions.createproject.CreateProjectResponse;
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

        Project project = Project.create(projectName);
        projectRepository.save(project);
        return Result.success(project);
    }

    public CreateProjectResponse withName(CreateProjectRequest request) {
        Result<Project> result = withName(request.projectName());
        if (result.isSuccess()) {
            Project project = result.values().getFirst();
            return new CreateProjectResponse("Project creation successful: " + project, project);
        } else {
            String allFailureMessages = String.join(", ", result.failureMessages());
            return new CreateProjectResponse("Project creation failed register these errors: " + allFailureMessages, null);
        }
    }
}
