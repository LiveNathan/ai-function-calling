package dev.nathanlively.application;

import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.TimesheetEntry;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateTimesheetEntryServiceTest {
    @Test
    void from() throws Exception {
        ResourceRepository repository = InMemoryResourceRepository.createEmpty();
        CreateTimesheetEntryService service = new CreateTimesheetEntryService(repository);

        Result<TimesheetEntry> actual = service.from

        assertThat(actual)
                .isEqualTo(expected);
    }
}