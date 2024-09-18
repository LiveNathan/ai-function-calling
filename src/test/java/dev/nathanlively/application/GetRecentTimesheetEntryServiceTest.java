package dev.nathanlively.application;

import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.application.port.UserRepository;
import dev.nathanlively.domain.*;
import dev.nathanlively.security.InMemoryUserRepository;
import dev.nathanlively.security.Role;
import dev.nathanlively.security.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;

import static dev.nathanlively.application.ResultAssertions.assertThat;

class GetRecentTimesheetEntryServiceTest {
    @Test
    void with() {
        String username = "nathanlively@gmail.com";
        String name = "Nathan Lively";
        Resource resource = Resource.create(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, name, username, null);
        TimesheetEntry timesheetEntry = TimesheetEntry.clockIn(new Project("Project A", 100), Instant.now());
        resource.appendTimesheetEntry(timesheetEntry);
//        User user = new User(username, name, "password", Collections.singleton(Role.USER), new byte[0]);
        ResourceRepository resourceRepository = InMemoryResourceRepository.createEmpty();
//        UserRepository userRepository = InMemoryUserRepository.createEmpty();
        resourceRepository.save(resource);
//        userRepository.save(user);

        GetRecentTimesheetEntryService service = new GetRecentTimesheetEntryService(resourceRepository);

        Result<TimesheetEntry> actual = service.with(username);

        assertThat(actual).isSuccess();
        assertThat(actual.failureMessages()).isEmpty();
        assertThat(actual).successValues().contains(timesheetEntry);
  