package dev.nathanlively.application;

import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.TimesheetEntry;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ClockInServiceTest {
    @Test
    void clockIn_givenNullProject() throws Exception {
        ResourceRepository resourceRepository = InMemoryResourceRepository.createEmpty();
        ClockInService service = new ClockInService(resourceRepository);
        String resourceEmail = "nathanlively@gmail.com";
        String projectName = "Project A (12345)";
        Instant clockInTime = Instant.now();
        TimesheetEntry expected = new TimesheetEntry(clockInTime, null, null, null);

        TimesheetEntry actual = service.clockIn(resourceEmail, clockInTime, projectName);

        assertThat(actual)
                .isEqualTo(expected);
    }
}