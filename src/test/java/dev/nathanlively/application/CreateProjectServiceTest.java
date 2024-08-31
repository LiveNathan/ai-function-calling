package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.domain.Project;
import org.junit.jupiter.api.Test;

import static dev.nathanlively.application.ResultAssertions.assertThat;

class CreateProjectServiceTest {
    @Test
    void withName() throws Exception {
        ProjectRepository repository = InMemoryProjectRepository.createEmpty();
        CreateProjectService service = new CreateProjectService(repository);
        String projectName = "Project B";
        Project expected = new Project(projectName);

        Result<Project> actual = service.withName(projectName);

        assertThat(actual).isSuccess();
    }
}