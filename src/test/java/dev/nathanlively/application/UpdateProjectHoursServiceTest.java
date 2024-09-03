package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.domain.Project;
import org.junit.jupiter.api.Test;

import static dev.nathanlively.application.ResultAssertions.assertThat;

class UpdateProjectHoursServiceTest {
    @Test
    void with() throws Exception {
        ProjectRepository projectRepository = InMemoryProjectRepository.createEmpty();
        String projectName = "Project A";
        projectRepository.save(Project.create(projectName));
        UpdateProjectHoursService service = new UpdateProjectHoursService(projectRepository);
        int estimatedHours = 100;

        Result<Project> actual = service.with(projectName, estimatedHours);

        assertThat(actual).isSuccess();
//        assertThat(actual.failureMessages()).isEmpty();
//        assertThat(actual).successValues().contains(expected);

//        List<Project> projects = repository.findAll();
//        Assertions.assertThat(projects).hasSize(1);
    }
}