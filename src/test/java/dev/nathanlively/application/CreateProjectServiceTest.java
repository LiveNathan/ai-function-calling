package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.domain.Project;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.nathanlively.application.ResultAssertions.assertThat;

class CreateProjectServiceTest {
    @Test
    void withName() throws Exception {
        ProjectRepository repository = InMemoryProjectRepository.createEmpty();
        CreateProjectService service = new CreateProjectService(repository);
        String projectName = "Project A (12345)";
        Project expected = new Project(projectName);

        Result<Project> actual = service.withName(projectName);

        assertThat(actual).isSuccess();
        assertThat(actual.failureMessages()).isEmpty();
        assertThat(actual).successValues().contains(expected);

        List<Project> projects = repository.findAll();
        Assertions.assertThat(projects).hasSize(1);
    }
}