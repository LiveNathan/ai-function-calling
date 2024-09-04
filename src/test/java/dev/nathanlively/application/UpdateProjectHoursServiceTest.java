package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.domain.Project;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.nathanlively.application.ResultAssertions.assertThat;

class UpdateProjectHoursServiceTest {
    @Test
    void with() throws Exception {
        ProjectRepository projectRepository = InMemoryProjectRepository.createEmpty();
        String projectName = "Project A";
        Project expected = Project.create(projectName);
        int estimatedHours = 100;
        expected.updateEstimatedHours(estimatedHours);
        projectRepository.save(expected);
        UpdateProjectHoursService service = new UpdateProjectHoursService(projectRepository);

        Result<Project> actual = service.with(projectName, estimatedHours);

        assertThat(actual).isSuccess();
        assertThat(actual.failureMessages()).isEmpty();
        assertThat(actual).successValues().contains(expected);

        List<Project> projects = projectRepository.findAll();
        Assertions.assertThat(projects).hasSize(1);
    }
}