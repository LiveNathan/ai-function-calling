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
        Project project = getProject(projectName);
        return resourceRepository.timesheetEntriesByProject(project);
    }

    private Project getProject(String projectName) {
        return projectRepository.findByName(projectName)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectName));
    }

    public List<String> allProjectNames() {
        return projectRepository.findAllNames();
    }

    public int totalHoursEstimated(String projectName) {
        Project project = getProject(projectName);
        return project.estimatedHours();
    }

    public float hoursConsumed(String projectName) {
        Project project = getProject(projectName);
        return resourceRepository.totalTimesheetEntryHours(project);
    }
}
