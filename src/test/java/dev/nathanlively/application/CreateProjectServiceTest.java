package dev.nathanlively.application;

import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.domain.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.nathanlively.application.ResultAssertions.assertThat;

class CreateProjectServiceTest {

    private final String projectNameA = "Project A";
    private ProjectRepository repository;
    private CreateProjectService service;

    @BeforeEach
    void setUp() {
        repository = InMemoryProjectRepository.createEmpty();
        service = new CreateProjectService(repository);
    }

    @Test
    void withName() throws Exception {
        Project expected = Project.create(projectNameA);

        Result<Project> actual = service.withName(projectNameA);

        assertThat(actual).isSuccess();
        assertThat(actual.failureMessages()).isEmpty();
        assertThat(actual).successValues().contains(expected);

        List<Project> projects = repository.findAll();
        assertThat(projects).hasSize(1);
    }

    @Test
    void withName_givenSimilarNames() throws Exception {
        Project projectA = Project.create(projectNameA);
        repository.save(projectA);
        String projectNameB = "Project Aa";

        Result<Project> actual = service.withName(projectNameB);

        assertThat(actual).isFailure().failureMessages().contains("There's already a project called " + projectNameA + ". Please create a more unique getName.");
    }
}