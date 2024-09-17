package dev.nathanlively.application;

import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.application.port.UserRepository;
import dev.nathanlively.domain.*;
import dev.nathanlively.security.AuthenticatedUser;
import dev.nathanlively.security.InMemoryUserRepository;
import dev.nathanlively.security.Role;
import dev.nathanlively.security.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetRecentTimesheetEntryServiceTest {
    @Test
    void with() throws Exception {
        String email = "nathanlively@gmail.com";
        String name = "Nathan Lively";
        Resource resource = Resource.create(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, name, email, null);
        TimesheetEntry timesheetEntry = TimesheetEntry.clockIn(new Project("Project A", 100), Instant.now());
        resource.appendTimesheetEntry(timesheetEntry);
        User user = new User(email, name, "password", Collections.singleton(Role.USER), new byte[0]);
        ResourceRepository repository = InMemoryResourceRepository.createEmpty();
        UserRepository userRepository = InMemoryUserRepository.createEmpty();
        repository.save(resource);
        userRepository.save(user);
        AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);
        when(authenticatedUser.get()).thenReturn(Optional.of(user));

        GetRecentTimesheetEntryService service = new GetRecentTimesheetEntryService(repository, authenticatedUser);

        Result<TimesheetEntry> actual = service.with();

        ResultAssertions.assertThat(actual).isSuccess();
    }

}