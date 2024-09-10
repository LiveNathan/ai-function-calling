package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Project;
import dev.nathanlively.domain.TimesheetEntry;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TimesheetEntriesPerProject {
    private final ResourceRepository repository;
    private final ProjectRepository projectRepository;

    public TimesheetEntriesPerProject(ResourceRepository repository, ProjectRepository projectRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
    }

    public List<TimesheetEntry> with(String projectName) {
        Objects.requireNonNull(projectName, "Project name cannot be null.");
        Optional<Project> optionalProject = projectRepository.findByName(projectName);
        if (optionalProject.isEmpty()) {
            return Collections.emptyList();
        }
        Project project = optionalProject.get();
        List<TimesheetEntry> entries = repository.timesheetEntriesByProject(project);
        return entries;
    }
}
