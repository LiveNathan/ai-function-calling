package dev.nathanlively.config;

import dev.nathanlively.application.ClockInService;
import dev.nathanlively.application.ClockOutService;
import dev.nathanlively.application.UpdateTimesheetEntryService;
import dev.nathanlively.application.functions.clockin.ClockInFunction;
import dev.nathanlively.application.functions.clockin.ClockInRequest;
import dev.nathanlively.application.functions.clockin.ClockInResponse;
import dev.nathanlively.application.functions.clockout.ClockOutFunction;
import dev.nathanlively.application.functions.clockout.ClockOutRequest;
import dev.nathanlively.application.functions.clockout.ClockOutResponse;
import dev.nathanlively.application.functions.findallprojectnames.FindAllProjectNamesFunction;
import dev.nathanlively.application.functions.findallprojectnames.FindAllProjectNamesRequest;
import dev.nathanlively.application.functions.findallprojectnames.FindAllProjectNamesResponse;
import dev.nathanlively.application.functions.updateproject.UpdateProjectFunction;
import dev.nathanlively.application.functions.updateproject.UpdateProjectRequest;
import dev.nathanlively.application.functions.updateproject.UpdateProjectResponse;
import dev.nathanlively.application.port.ProjectRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class FunctionConfig {
    @Bean
    @Description("Create a new timesheet entry for a resource. The project name is important, but optional. If the project name is known, include it. Otherwise, it can be set to null, and the project can be updated later using the updateProjectFunction.")
    public Function<ClockInRequest, ClockInResponse> clockInFunction(ClockInService clockInService) {
        return new ClockInFunction(clockInService);
    }

    @Bean
    @Description("Update an existing timesheet entry for a resource. If the resource is found, it updates the most recent timesheet entry with the clock-out time and saves the resource. If there are any issues, appropriate error messages are returned.")
    public Function<ClockOutRequest, ClockOutResponse> clockOutFunction(ClockOutService clockOutService) {
        return new ClockOutFunction(clockOutService);
    }

    @Bean
    @Description("Update an existing timesheet entry's project. Use this function in the case when a user clocks in without a project and now wants to add the project to it. The project name must exactly match one of the project names that already exists in the repository. Use findAllProjectNamesFunction to fetch the available names.")
    public Function<UpdateProjectRequest, UpdateProjectResponse> updateProjectFunction(UpdateTimesheetEntryService updateTimesheetEntryService) {
        return new UpdateProjectFunction(updateTimesheetEntryService);
    }

    @Bean
    @Description("Fetch the list of available project names.")
    public Function<FindAllProjectNamesRequest, FindAllProjectNamesResponse> findAllProjectNamesFunction(ProjectRepository repository) {
        return new FindAllProjectNamesFunction(repository);
    }

}
