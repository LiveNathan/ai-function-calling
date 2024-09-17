package dev.nathanlively.application;

import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.JobTitle;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.ResourceType;
import dev.nathanlively.domain.TimesheetEntry;
import org.junit.jupiter.api.Test;

class GetRecentTimesheetEntryServiceTest {
    @Test
    void with() throws Exception {
        String resourceEmail = "nathanlively@gmail.com";
        Resource resource = Resource.create(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", resourceEmail, null);
        ResourceRepository repository = InMemoryResourceRepository.createEmpty();
        repository.save(resource);
        GetRecentTimesheetEntryService service = new GetRecentTimesheetEntryService(repository);

        Result<TimesheetEntry> actual = service.with(resourceEmail);

        ResultAssertions.assertThat(actual).isSuccess();
    }

}