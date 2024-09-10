package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Project;
import dev.nathanlively.domain.TimesheetEntry;

import java.util.List;
import java.util.Objects;

public class TimesheetEntriesPerProject {
    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;

    public TimesheetEntriesPerProject(ResourceRepository resourceRepository, ProjectRepository projectRepository) {
        this.resourceRepository = resourceRepository;
        this.projectRepository = projectRepository;
    }

    public List<TimesheetEntry> with(String projectName) {
        Objects.requireNonNull(projectName, "Project name cannot be null.");
        Project project = projectRepository.findByName(projectName)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectName));
        return resourceRepository.timesheetEntriesByProject(project);
    }

    public List<String> allProjectNames() {
        return projectRepository.findAllNames();
    }
}
