package dev.nathanlively.config;

import dev.nathanlively.application.ClockInFunction;
import dev.nathanlively.application.ClockInService;
import dev.nathanlively.application.FindAllProjectNamesFunction;
import dev.nathanlively.application.UpdateProjectFunction;
import dev.nathanlively.application.clockin.ClockInRequest;
import dev.nathanlively.application.clockin.ClockInResponse;
import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.updateproject.UpdateProjectRequest;
import dev.nathanlively.application.updateproject.UpdateProjectResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.function.Function;

@Configuration
public class FunctionConfig {
    @Bean
    @Description("Create a new timesheet entry for a resource. The project name is important, but optional. If the project name is known, include it. Otherwise, it can be set to null, and the project can be updated later using the updateProjectFunction.")
    public Function<ClockInRequest, ClockInResponse> clockInFunction(ClockInService clockInService) {
        return new ClockInFunction(clockInService);
    }

    @Bean
    @Description("Update an existing timesheet entry's project. Use this function in the case when a user clocks in without a project and now wants to add the project to it. The project name must exactly match one of the project names that already exists in the repository. Use findAllProjectNamesFunction to fetch the available names.")
    public Function<UpdateProjectRequest, UpdateProjectResponse> updateProjectFunction(ClockInService clockInService) {
        return new UpdateProjectFunction(clockInService);
    }

    @Bean
    @Description("Fetch the list of available project names.")
    public Function<Void, List<String>> findAllProjectNamesFunction(ProjectRepository repository) {
        return new FindAllProjectNamesFunction(repository);
    }
}
