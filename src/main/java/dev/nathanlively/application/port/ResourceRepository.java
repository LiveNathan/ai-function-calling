package dev.nathanlively.application.port;

import dev.nathanlively.domain.Project;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository {
    void save(Resource resource);

    List<Resource> findAll();

    Optional<Resource> findByEmail(String resourceEmail);

    List<TimesheetEntry> timesheetEntriesByProject(Project project);
}
