package dev.nathanlively.application;

import com.vaadin.flow.spring.security.AuthenticationContext;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.application.port.UserRepository;
import dev.nathanlively.domain.*;
import dev.nathanlively.security.AuthenticatedUser;
import dev.nathanlively.security.InMemoryUserRepository;
import dev.nathanlively.security.Role;
import dev.nathanlively.security.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static dev.nathanlively.application.ResultAssertions.assertThat;

class GetRecentTimesheetEntryServiceTest {
    @Test
    void with() {
        String email = "nathanlively@gmail.com";
        String name = "Nathan Lively";
        Resource resource = Resource.create(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, name, email, null);
        TimesheetEntry timesheetEntry = TimesheetEntry.clockIn(new Project("Project A", 100), Instant.now());
        resource.appendTimesheetEntry(timesheetEntry);
        User user = new User(email, name, "password", Collections.singleton(Role.USER), new byte[0]);
        ResourceRepository resourceRepository = InMemoryResourceRepository.createEmpty();
        UserRepository userRepository = InMemoryUserRepository.createEmpty();
        resourceRepository.save(resource);
        userRepository.save(user);
        TestAuthenticationContext testAuthCtx = new TestAuthenticationContext(user);
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(testAuthCtx, userRepository);

        GetRecentTimesheetEntryService service = new GetRecentTimesheetEntryService(resourceRepository, authenticatedUser);

        Result<TimesheetEntry> actual = service.with();

        assertThat(actual).isSuccess();
        assertThat(actual.failureMessages()).isEmpty();
        assertThat(actual).successValues().contains(timesheetEntry);
    }

    private static class TestAuthenticationContext extends AuthenticationContext {
        private final User user;

        public TestAuthenticationContext(User user) {
            this.user = user;
        }

        @Override
        public <U> Optional<U> getAuthenticatedUser(Class<U> userType) {
            if (UserDetails.class.equals(userType)) {
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                        user.getUsername(), "password", new ArrayList<>());
                return Optional.of(userType.cast(userDetails));
            }
            return Optional.empty();
        }
    }

}