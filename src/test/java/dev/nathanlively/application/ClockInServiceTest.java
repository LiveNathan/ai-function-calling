package dev.nathanlively.application;

import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ClockInServiceTest {

    @Test
    void clockIn_givenNullProject() throws Exception {
        ResourceRepository resourceRepository = InMemoryResourceRepository.createEmpty();
        String resourceEmail = "nathanlively@gmail.com";
        Resource resource = new Resource(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", resourceEmail, null);
        resourceRepository.save(resource);
        assertThat(resourceRepository.findAll().getFirst().timeSheet().timeSheetEntries()).isEmpty();
        ClockInService service = new ClockInService(resourceRepository);
        String projectName = "Project A (12345)";
        Instant clockInTime = Instant.now();
        Project project = new Project(projectName, null);
        TimesheetEntry expected = TimesheetEntry.clockIn(clockInTime);

        TimesheetEntry actual = service.clockIn(resourceEmail, clockInTime, null);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);

//        List<Resource> resources = resourceRepository.findAll();
//        assertThat(resources).hasSize(1);
//        assertThat(resources.getFirst().timeSheet().timeSheetEntries())
//                .hasSize(1);
    }
}