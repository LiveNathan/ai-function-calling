package dev.nathanlively.adapter.out.eclipse;

import dev.nathanlively.domain.DataRoot;
import dev.nathanlively.domain.Project;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("until I learn how to test EclipseStore")
@SpringBootTest(classes = TestConfig.class)
@Tag("database")
class EclipseProjectAdapterTest {
    @Autowired
    EmbeddedStorageManager storageManager;
    EclipseProjectAdapter adapter;

    @BeforeEach
    void setUp() {
        storageManager.start();
        storageManager.setRoot(new DataRoot());
        adapter = new EclipseProjectAdapter(storageManager);
    }

    @AfterEach
    void tearDown() {
        storageManager.shutdown(); // Shutdown the storage manager if running
    }

    @Test
    void canReadAndWrite() throws Exception {
        List<Project> projects = adapter.findAll();
        assertThat(projects).isEmpty();

        Project project = Project.create("Project A");
        adapter.save(project);
        List<Project> allProjects = adapter.findAll();
        assertThat(allProjects).hasSize(1);
        assertThat(allProjects.getFirst().name()).isEqualTo("Project A");
    }
}